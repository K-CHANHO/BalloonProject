package org.zerock.balloon.dto.free;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoardDTO {

    private Long bno;

    private String subject;

    private String title;

    private String content;

    private String id;

    private String nickname;

    private Long hits;

    private Long good;

    private Long replyCount;

    private LocalDateTime regDate, modDate;

    @Builder.Default
    private List<FreeBoardImageDTO> imageDTOList = new ArrayList<>();
}
