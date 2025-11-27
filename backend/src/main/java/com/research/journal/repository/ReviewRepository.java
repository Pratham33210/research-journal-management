package com.research.journal.repository;

import com.research.journal.entity.Review;
import com.research.journal.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPaperId(Long paperId);
    List<Review> findByReviewerId(Long reviewerId);
    List<Review> findByPaperIdAndStatus(Long paperId, ReviewStatus status);
    List<Review> findByReviewerIdAndStatus(Long reviewerId, ReviewStatus status);
}
