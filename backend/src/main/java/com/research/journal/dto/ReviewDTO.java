package com.research.journal.dto;

import com.research.journal.entity.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private Long paperId;
    private UserDTO reviewer;
    private ReviewStatus status;
    private String comments;
    private Integer overallRating;
    private Integer technicalQualityRating;
    private Integer clarityRating;
    private Integer originalityRating;
    private Integer significanceRating;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
