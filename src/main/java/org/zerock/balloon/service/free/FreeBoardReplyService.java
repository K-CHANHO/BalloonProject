package org.zerock.balloon.service.free;

import org.zerock.balloon.dto.free.FreeBoardReplyDTO;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.free.FreeBoardReply;

import java.util.List;


public interface FreeBoardReplyService {

    void register(FreeBoardReplyDTO freeBoardReplyDTO); // 댓글 등록

    List<FreeBoardReplyDTO> getList(Long bno); // 특정 게시물의 댓글 목록

    void modify(FreeBoardReplyDTO freeBoardReplyDTO); // 댓글 수정

    void remove(Long rno); // 댓글 삭제


    default FreeBoardReplyDTO entityToDTO(FreeBoardReply freeBoardReply){

        FreeBoardReplyDTO dto = FreeBoardReplyDTO.builder()
                .bno(freeBoardReply.getFreeBoard().getBno())
                .rno(freeBoardReply.getRno())
                .rcontent(freeBoardReply.getContent())
                .id(freeBoardReply.getId())
                .nickname(freeBoardReply.getNickname())
                .regDate(freeBoardReply.getRegDate())
                .modDate(freeBoardReply.getModDate())
                .build();

        return dto;
    }

    default FreeBoardReply dtoToEntity(FreeBoardReplyDTO freeBoardReplyDTO){

        FreeBoard freeBoard = FreeBoard.builder()
                .bno(freeBoardReplyDTO.getBno())
                .build();

        FreeBoardReply entity = FreeBoardReply.builder()
                .freeBoard(freeBoard)
                .rno(freeBoardReplyDTO.getRno())
                .content(freeBoardReplyDTO.getRcontent())
                .id(freeBoardReplyDTO.getId())
                .nickname(freeBoardReplyDTO.getNickname())
                .build();

        return entity;
    }
}
