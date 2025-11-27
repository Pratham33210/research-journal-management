package com.research.journal.repository;

import com.research.journal.entity.Revision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {
    List<Revision> findByPaperId(Long paperId);
    List<Revision> findByPaperIdOrderByRevisionNumberDesc(Long paperId);
}
