package org.zerock.balloon.dto.recom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecomReplyDTO {
    private Long rno;
    private String content;
    private String nickname;
    private Long bno;
    private LocalDateTime regDate, modDate;
    private String id;
}
