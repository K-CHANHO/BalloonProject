package org.zerock.balloon.service.qna;

import org.zerock.balloon.dto.qna.QnaReplyDTO;
import org.zerock.balloon.entity.qna.QnaBoard;
import org.zerock.balloon.entity.qna.QnaReply;
import org.zerock.balloon.entity.Member;

import java.util.List;

public interface QnaReplyService {
    Long register(QnaReplyDTO qnaReplyDTO); //댓글의 등록

    List<QnaReplyDTO> getList(Long qbno); //특정 게시물의 댓글 목록

    void modify(QnaReplyDTO qnaReplyDTO); //댓글 수정

    void remove(Long qbrno); //댓글 삭제

    default QnaReply dtoToEntity(QnaReplyDTO qnaReplyDTO){

        QnaBoard qnaBoard = QnaBoard.builder().qbno(qnaReplyDTO.getQbno()).build();

        QnaReply qnaReply = QnaReply.builder()
                .qbrno(qnaReplyDTO.getQbrno())
                .content(qnaReplyDTO.getContent())
                .id(qnaReplyDTO.getId())
                .nickname(qnaReplyDTO.getNickname())
                .qnaBoard(qnaBoard)
                .build();

        return qnaReply;
    }

    //Reply객체를 ReplyDTO로 변환 Board 객체가 필요하지 않으므로 게시물 번호만
    default QnaReplyDTO entityToDTO(QnaReply qnaReply){
        QnaReplyDTO dto = QnaReplyDTO.builder()
                .qbrno(qnaReply.getQbrno())
                .content(qnaReply.getContent())
                .id(qnaReply.getId())
                .nickname(qnaReply.getNickname())
                .regDate(qnaReply.getRegDate())
                .modDate(qnaReply.getModDate())
                .build();

        return dto;

    }
}
