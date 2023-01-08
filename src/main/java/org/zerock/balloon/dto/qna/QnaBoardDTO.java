package org.zerock.balloon.dto.qna;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaBoardDTO {
    private Long qbno;
    private String qbtitle;
    private String qbcontent;
    private String nickname;
    private String id;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Long qbhits;
    private int qbreplyCount;
}
