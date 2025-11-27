package com.research.journal.service;

import com.research.journal.dto.PaperDTO;
import com.research.journal.entity.Paper;
import com.research.journal.entity.PaperStatus;
import com.research.journal.entity.User;
import com.research.journal.repository.PaperRepository;
import com.research.journal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaperService {

    private final PaperRepository paperRepository;
    private final UserRepository userRepository;

    public PaperDTO submitPaper(String title, String abstractText, String content, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Paper paper = Paper.builder()
                .title(title)
                .abstractText(abstractText)
                .content(content)
                .author(author)
                .status(PaperStatus.SUBMITTED)
                .plagiarismChecked(false)
                .build();

        Paper savedPaper = paperRepository.save(paper);
        return convertToDTO(savedPaper);
    }

    public PaperDTO getPaperById(Long id) {
        Paper paper = paperRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paper not found"));
        return convertToDTO(paper);
    }

    public List<PaperDTO> getPapersByAuthor(Long authorId) {
        return paperRepository.findByAuthorId(authorId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PaperDTO> getPapersByStatus(PaperStatus status) {
        return paperRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<PaperDTO> getAllPapersOrderedBySubmission() {
        return paperRepository.findAll().stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .map(this::convertToDTO)
                .toList();
    }

    public PaperDTO updatePaperStatus(Long paperId, PaperStatus newStatus) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found"));

        paper.setStatus(newStatus);
        
        if (newStatus == PaperStatus.ACCEPTED) {
            paper.setAcceptedAt(LocalDateTime.now());
        } else if (newStatus == PaperStatus.REJECTED) {
            paper.setRejectedAt(LocalDateTime.now());
        } else if (newStatus == PaperStatus.PUBLISHED) {
            paper.setPublishedAt(LocalDateTime.now());
        }

        Paper updatedPaper = paperRepository.save(paper);
        return convertToDTO(updatedPaper);
    }

    public PaperDTO setPlagiarismScore(Long paperId, Double score) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found"));

        paper.setPlagiarismScore(score);
        paper.setPlagiarismChecked(true);

        Paper updatedPaper = paperRepository.save(paper);
        return convertToDTO(updatedPaper);
    }

    public void deletePaper(Long paperId) {
        if (!paperRepository.existsById(paperId)) {
            throw new RuntimeException("Paper not found");
        }
        paperRepository.deleteById(paperId);
    }

    private PaperDTO convertToDTO(Paper paper) {
        return PaperDTO.builder()
                .id(paper.getId())
                .title(paper.getTitle())
                .abstractText(paper.getAbstractText())
                .content(paper.getContent())
                .status(paper.getStatus())
                .plagiarismScore(paper.getPlagiarismScore())
                .plagiarismChecked(paper.getPlagiarismChecked())
                .submittedAt(paper.getSubmittedAt())
                .acceptedAt(paper.getAcceptedAt())
                .rejectedAt(paper.getRejectedAt())
                .publishedAt(paper.getPublishedAt())
                .createdAt(paper.getCreatedAt())
                .updatedAt(paper.getUpdatedAt())
                .build();
    }
}
