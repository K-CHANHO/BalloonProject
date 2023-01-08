package org.zerock.balloon.dto.community;

import lombok.*;

import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommunityBoardDTO {

    private Long bno; // 게시물 번호

    private String subject; // 게시물 말머리

    private String title; // 게시물 제목

    private String content; // 게시물 내용

    private String id; // 게시물 작성자 id

    private String nickname; // 게시물 작성자 닉네임

    private Long hits; // 게시물 조회수

    private Long good; // 게시물 좋아요 수

    private Long replyCount; // 게시물 댓글 수

    private LocalDateTime regDate, modDate; // 게시물 작성, 수정 시간

    @Builder.Default
    private List<CommunityBoardImageDTO> imageDTOList = new ArrayList<>();
}
