package org.zerock.balloon.dto.recom;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecomBoardDTO {
    private Long bno;
    private String subject;
    private String title;
    private String content;
    @Builder.Default
    private List<RecomBoardImageDTO> imageDTOList = new ArrayList<>();
    private String nickname;
    private String id;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Long hits;
    private Long good;
    private Long hate;
    private int replyCount;
}
