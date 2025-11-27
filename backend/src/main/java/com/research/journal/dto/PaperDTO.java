package com.research.journal.dto;

import com.research.journal.entity.PaperStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaperDTO {
    private Long id;
    private String title;
    private String abstractText;
    private String content;
    private UserDTO author;
    private PaperStatus status;
    private Double plagiarismScore;
    private Boolean plagiarismChecked;
    private List<ReviewDTO> reviews;
    private List<RevisionDTO> revisions;
    private LocalDateTime submittedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
