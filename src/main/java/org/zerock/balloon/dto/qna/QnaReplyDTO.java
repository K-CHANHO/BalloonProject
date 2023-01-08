package org.zerock.balloon.dto.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QnaReplyDTO {
    private Long qbrno;
    private String content;
    private String nickname;
    private Long qbno;
    private LocalDateTime regDate, modDate;
    private String id;
}
