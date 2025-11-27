package com.research.journal.service;

import com.research.journal.dto.RevisionDTO;
import com.research.journal.entity.Paper;
import com.research.journal.entity.Revision;
import com.research.journal.repository.PaperRepository;
import com.research.journal.repository.RevisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RevisionService {

    private final RevisionRepository revisionRepository;
    private final PaperRepository paperRepository;

    public RevisionDTO submitRevision(Long paperId, String content, String changesSummary) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found"));

        // Get the next revision number
        List<Revision> existingRevisions = revisionRepository.findByPaperId(paperId);
        Integer nextRevisionNumber = existingRevisions.isEmpty() ? 1 : 
                existingRevisions.stream()
                    .mapToInt(Revision::getRevisionNumber)
                    .max()
                    .orElse(0) + 1;

        Revision revision = Revision.builder()
                .paper(paper)
                .revisionNumber(nextRevisionNumber)
                .content(content)
                .changesSummary(changesSummary)
                .submittedAt(LocalDateTime.now())
                .build();

        Revision savedRevision = revisionRepository.save(revision);
        return convertToDTO(savedRevision);
    }

    public RevisionDTO getRevisionById(Long id) {
        Revision revision = revisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revision not found"));
        return convertToDTO(revision);
    }

    public List<RevisionDTO> getRevisionsByPaper(Long paperId) {
        return revisionRepository.findByPaperId(paperId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<RevisionDTO> getRevisionsByPaperOrderedByVersion(Long paperId) {
        return revisionRepository.findByPaperIdOrderByRevisionNumberDesc(paperId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public void deleteRevision(Long revisionId) {
        if (!revisionRepository.existsById(revisionId)) {
            throw new RuntimeException("Revision not found");
        }
        revisionRepository.deleteById(revisionId);
    }

    private RevisionDTO convertToDTO(Revision revision) {
        return RevisionDTO.builder()
                .id(revision.getId())
                .revisionNumber(revision.getRevisionNumber())
                .content(revision.getContent())
                .changesSummary(revision.getChangesSummary())
                .submittedAt(revision.getSubmittedAt())
                .createdAt(revision.getCreatedAt())
                .updatedAt(revision.getUpdatedAt())
                .build();
    }
}
