package org.zerock.balloon.service.recom;

import org.zerock.balloon.dto.recom.RecomBoardReplyDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.entity.recom.RecomBoardReply;

import java.util.List;

public interface RecomBoardReplyService {
    void register(RecomBoardReplyDTO communityBoardReplyDTO); // 댓글 등록

    List<RecomBoardReplyDTO> getList(Long bno); // 특정 게시물의 댓글 목록

    void modify(RecomBoardReplyDTO communityBoardReplyDTO); // 댓글 수정

    void remove(Long rno); // 댓글 삭제


    default RecomBoardReplyDTO entityToDTO(RecomBoardReply recomBoardReply){

        RecomBoardReplyDTO dto = RecomBoardReplyDTO.builder()
                .bno(recomBoardReply.getRecomBoard().getBno())
                .rno(recomBoardReply.getRno())
                .rcontent(recomBoardReply.getContent())
                .id(recomBoardReply.getId())
                .nickname(recomBoardReply.getNickname())
                .regDate(recomBoardReply.getRegDate())
                .modDate(recomBoardReply.getModDate())
                .build();

        return dto;
    }

    default RecomBoardReply dtoToEntity(RecomBoardReplyDTO recomBoardReplyDTO){

        RecomBoard recomBoard = RecomBoard.builder()
                .bno(recomBoardReplyDTO.getBno())
                .build();

        RecomBoardReply entity = RecomBoardReply.builder()
                .recomBoard(recomBoard)
                .rno(recomBoardReplyDTO.getRno())
                .content(recomBoardReplyDTO.getRcontent())
                .id(recomBoardReplyDTO.getId())
                .nickname(recomBoardReplyDTO.getNickname())
                .build();

        return entity;
    }
}
