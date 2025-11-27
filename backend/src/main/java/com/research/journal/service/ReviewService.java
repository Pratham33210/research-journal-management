package com.research.journal.service;

import com.research.journal.dto.ReviewDTO;
import com.research.journal.entity.Paper;
import com.research.journal.entity.Review;
import com.research.journal.entity.ReviewStatus;
import com.research.journal.entity.User;
import com.research.journal.repository.PaperRepository;
import com.research.journal.repository.ReviewRepository;
import com.research.journal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PaperRepository paperRepository;
    private final UserRepository userRepository;

    public ReviewDTO submitReview(Long paperId, Long reviewerId, String comments,
                                  Integer overallRating, Integer technicalQualityRating,
                                  Integer clarityRating, Integer originalityRating,
                                  Integer significanceRating) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found"));

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        Review review = Review.builder()
                .paper(paper)
                .reviewer(reviewer)
                .status(ReviewStatus.SUBMITTED)
                .comments(comments)
                .overallRating(overallRating)
                .technicalQualityRating(technicalQualityRating)
                .clarityRating(clarityRating)
                .originalityRating(originalityRating)
                .significanceRating(significanceRating)
                .submittedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return convertToDTO(review);
    }

    public List<ReviewDTO> getReviewsByPaper(Long paperId) {
        return reviewRepository.findByPaperId(paperId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ReviewDTO> getReviewsByReviewer(Long reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ReviewDTO> getPendingReviewsForReviewer(Long reviewerId) {
        return reviewRepository.findByReviewerIdAndStatus(reviewerId, ReviewStatus.PENDING).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ReviewDTO updateReviewStatus(Long reviewId, ReviewStatus newStatus) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setStatus(newStatus);

        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }

    private ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .status(review.getStatus())
                .comments(review.getComments())
                .overallRating(review.getOverallRating())
                .technicalQualityRating(review.getTechnicalQualityRating())
                .clarityRating(review.getClarityRating())
                .originalityRating(review.getOriginalityRating())
                .significanceRating(review.getSignificanceRating())
                .submittedAt(review.getSubmittedAt())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
