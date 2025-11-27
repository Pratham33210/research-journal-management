package com.research.journal.controller;

import com.research.journal.dto.PaperDTO;
import com.research.journal.entity.PaperStatus;
import com.research.journal.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/papers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaperController {

    private final PaperService paperService;

    @PostMapping
    public ResponseEntity<?> submitPaper(@RequestBody PaperDTO request) {
        try {
            // Extract user ID from JWT token (in production, use SecurityContext)
            Long userId = 1L; // TODO: Get from authenticated user
            
            PaperDTO paperDTO = paperService.submitPaper(
                    request.getTitle(),
                    request.getAbstractText(),
                    request.getContent(),
                    userId
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(paperDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaper(@PathVariable Long id) {
        try {
            PaperDTO paper = paperService.getPaperById(id);
            return ResponseEntity.ok(paper);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> getPapersByAuthor(@PathVariable Long authorId) {
        try {
            List<PaperDTO> papers = paperService.getPapersByAuthor(authorId);
            return ResponseEntity.ok(papers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPapersByStatus(@PathVariable String status) {
        try {
            PaperStatus paperStatus = PaperStatus.valueOf(status.toUpperCase());
            List<PaperDTO> papers = paperService.getPapersByStatus(paperStatus);
            return ResponseEntity.ok(papers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid paper status: " + status);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPapers() {
        try {
            List<PaperDTO> papers = paperService.getAllPapersOrderedBySubmission();
            return ResponseEntity.ok(papers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updatePaperStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            PaperStatus paperStatus = PaperStatus.valueOf(status.toUpperCase());
            PaperDTO updatedPaper = paperService.updatePaperStatus(id, paperStatus);
            return ResponseEntity.ok(updatedPaper);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid paper status: " + status);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/plagiarism")
    public ResponseEntity<?> setPlagiarismScore(@PathVariable Long id, @RequestParam Double score) {
        try {
            if (score < 0 || score > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Plagiarism score must be between 0 and 100");
            }
            PaperDTO updatedPaper = paperService.setPlagiarismScore(id, score);
            return ResponseEntity.ok(updatedPaper);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaper(@PathVariable Long id) {
        try {
            paperService.deletePaper(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
