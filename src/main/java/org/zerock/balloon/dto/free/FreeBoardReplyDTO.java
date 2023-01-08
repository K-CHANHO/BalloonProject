package org.zerock.balloon.dto.free;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FreeBoardReplyDTO {

    private Long bno;

    private Long rno;

    private String rcontent;

    private String id;

    private String nickname;

    private LocalDateTime regDate, modDate;
}
