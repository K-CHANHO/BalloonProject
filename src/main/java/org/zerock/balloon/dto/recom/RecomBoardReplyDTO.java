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
public class RecomBoardReplyDTO {
    private Long bno; // 게시물 번호

    private Long rno; // 댓글 번호

    private String rcontent; // 댓글 내용

    private String id; // 작성자 id

    private String nickname; // 작성자 닉네임

    private LocalDateTime regDate, modDate; // 댓글 작성, 수정 시간
}
