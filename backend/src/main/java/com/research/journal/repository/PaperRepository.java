package com.research.journal.repository;

import com.research.journal.entity.Paper;
import com.research.journal.entity.PaperStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    List<Paper> findByAuthorId(Long authorId);
    List<Paper> findByStatus(PaperStatus status);
    
    @Query("SELECT p FROM Paper p WHERE p.status = :status ORDER BY p.submittedAt DESC")
    List<Paper> findByStatusOrderBySubmittedAtDesc(@Param("status") PaperStatus status);
    
    @Query("SELECT p FROM Paper p WHERE p.author.id = :authorId ORDER BY p.submittedAt DESC")
    List<Paper> findAuthorPapersOrderBySubmittedAtDesc(@Param("authorId") Long authorId);
}
