package com.research.journal.controller;

import com.research.journal.dto.RevisionDTO;
import com.research.journal.service.RevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/revisions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RevisionController {

    private final RevisionService revisionService;

    @PostMapping
    public ResponseEntity<?> submitRevision(@RequestBody RevisionDTO request) {
        try {
            RevisionDTO revisionDTO = revisionService.submitRevision(
                    request.getPaperId(),
                    request.getContent(),
                    request.getChangesSummary()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(revisionDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRevision(@PathVariable Long id) {
        try {
            RevisionDTO revision = revisionService.getRevisionById(id);
            return ResponseEntity.ok(revision);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/paper/{paperId}")
    public ResponseEntity<?> getRevisionsByPaper(@PathVariable Long paperId) {
        try {
            List<RevisionDTO> revisions = revisionService.getRevisionsByPaper(paperId);
            return ResponseEntity.ok(revisions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/paper/{paperId}/ordered")
    public ResponseEntity<?> getRevisionsByPaperOrdered(@PathVariable Long paperId) {
        try {
            List<RevisionDTO> revisions = revisionService.getRevisionsByPaperOrderedByVersion(paperId);
            return ResponseEntity.ok(revisions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRevision(@PathVariable Long id) {
        try {
            revisionService.deleteRevision(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
