package org.zerock.balloon.service.community;

import org.zerock.balloon.dto.community.CommunityBoardReplyDTO;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardReply;

import java.util.List;

public interface CommunityBoardReplyService {

    void register(CommunityBoardReplyDTO communityBoardReplyDTO); // 댓글 등록

    List<CommunityBoardReplyDTO> getList(Long bno); // 특정 게시물의 댓글 목록

    void modify(CommunityBoardReplyDTO communityBoardReplyDTO); // 댓글 수정

    void remove(Long rno); // 댓글 삭제


    default CommunityBoardReplyDTO entityToDTO(CommunityBoardReply communityBoardReply){

        CommunityBoardReplyDTO dto = CommunityBoardReplyDTO.builder()
                .bno(communityBoardReply.getCommunityBoard().getBno())
                .rno(communityBoardReply.getRno())
                .rcontent(communityBoardReply.getContent())
                .id(communityBoardReply.getId())
                .nickname(communityBoardReply.getNickname())
                .regDate(communityBoardReply.getRegDate())
                .modDate(communityBoardReply.getModDate())
                .build();

        return dto;
    }

    default CommunityBoardReply dtoToEntity(CommunityBoardReplyDTO communityBoardReplyDTO){

        CommunityBoard communityBoard = CommunityBoard.builder()
                .bno(communityBoardReplyDTO.getBno())
                .build();

        CommunityBoardReply entity = CommunityBoardReply.builder()
                .communityBoard(communityBoard)
                .rno(communityBoardReplyDTO.getRno())
                .content(communityBoardReplyDTO.getRcontent())
                .id(communityBoardReplyDTO.getId())
                .nickname(communityBoardReplyDTO.getNickname())
                .build();

        return entity;
    }
}
