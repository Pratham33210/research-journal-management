package com.research.journal.controller;

import com.research.journal.dto.ReviewDTO;
import com.research.journal.entity.ReviewStatus;
import com.research.journal.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> submitReview(@RequestBody ReviewDTO request) {
        try {
            // Extract reviewer ID from JWT token (in production, use SecurityContext)
            Long reviewerId = 1L; // TODO: Get from authenticated user
            
            ReviewDTO reviewDTO = reviewService.submitReview(
                    request.getPaperId(),
                    reviewerId,
                    request.getComments(),
                    request.getOverallRating(),
                    request.getTechnicalQualityRating(),
                    request.getClarityRating(),
                    request.getOriginalityRating(),
                    request.getSignificanceRating()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReview(@PathVariable Long id) {
        try {
            ReviewDTO review = reviewService.getReviewById(id);
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/paper/{paperId}")
    public ResponseEntity<?> getReviewsByPaper(@PathVariable Long paperId) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByPaper(paperId);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<?> getReviewsByReviewer(@PathVariable Long reviewerId) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByReviewer(reviewerId);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/reviewer/{reviewerId}/pending")
    public ResponseEntity<?> getPendingReviews(@PathVariable Long reviewerId) {
        try {
            List<ReviewDTO> reviews = reviewService.getPendingReviewsForReviewer(reviewerId);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateReviewStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            ReviewStatus reviewStatus = ReviewStatus.valueOf(status.toUpperCase());
            ReviewDTO updatedReview = reviewService.updateReviewStatus(id, reviewStatus);
            return ResponseEntity.ok(updatedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid review status: " + status);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
