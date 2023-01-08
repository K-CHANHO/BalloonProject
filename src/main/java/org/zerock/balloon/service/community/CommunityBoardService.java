package org.zerock.balloon.service.community;

import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.PageResultDTO;
import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.dto.community.CommunityBoardImageDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface CommunityBoardService {
    PageResultDTO<CommunityBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); // 게시판 목록

    List<CommunityBoardDTO> getTopList(); // 게시판 상위 목록
    List<CommunityBoardDTO> getTopFive();

    Long register(CommunityBoardDTO communityBoardDTO); // 게시물 등록

    CommunityBoardDTO get(Long bno); // 게시물 상세보기

    void modify(CommunityBoardDTO communityBoardDTO); // 게시물 수정

    void remove(Long bno);

    void updateHits(Long bno); // 조회수

    default Map<String, Object> dtoToEntity(CommunityBoardDTO communityBoardDTO){

        Map<String, Object> entityMap = new HashMap<>();

        Member member = Member.builder()
                .id(communityBoardDTO.getId())
                .build();

        CommunityBoard communityBoard = CommunityBoard.builder()
                .bno(communityBoardDTO.getBno())
                .subject(communityBoardDTO.getSubject())
                .title(communityBoardDTO.getTitle())
                .content(communityBoardDTO.getContent())
                .member(member)
                .hits(communityBoardDTO.getHits())
                .good(communityBoardDTO.getGood())
                .build();
        entityMap.put("board", communityBoard);

        List<CommunityBoardImageDTO> imageDTOList = communityBoardDTO.getImageDTOList();
        if(imageDTOList != null && imageDTOList.size() > 0){
            List<CommunityBoardImage> communityBoardImageList = imageDTOList.stream().map(communityBoardImageDTO -> {
                CommunityBoardImage communityBoardImage = CommunityBoardImage.builder()
                        .path(communityBoardImageDTO.getPath())
                        .name(communityBoardImageDTO.getName())
                        .uuid(communityBoardImageDTO.getUuid())
                        .communityBoard(communityBoard)
                        .build();
                return communityBoardImage;
            }).collect(Collectors.toList());
            entityMap.put("imgList", communityBoardImageList);
        }

        return entityMap;

    }

    default CommunityBoardDTO entityToDTO(CommunityBoard board, Member member, Long replyCount){

        CommunityBoardDTO boardDTO = CommunityBoardDTO.builder()
                .bno(board.getBno())
                .subject(board.getSubject())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .hits(board.getHits())
                .good(board.getGood())
                .replyCount(replyCount.longValue())
                .id(member.getId())
                .nickname(member.getNickname())
                .build();
        return boardDTO;
    }

    default CommunityBoardDTO entityToDTOWithImg(CommunityBoard board, Member member, List<CommunityBoardImage> Images, Long replyCount){

        CommunityBoardDTO boardDTO = CommunityBoardDTO.builder()
                .bno(board.getBno())
                .subject(board.getSubject())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .hits(board.getHits())
                .good(board.getGood())
                .replyCount(replyCount.longValue())
                .id(member.getId())
                .nickname(member.getNickname())
                .build();

        List<CommunityBoardImageDTO> communityBoardImageDTOList =Images.stream().map(walkRecommendBoardImage -> {
            return CommunityBoardImageDTO.builder()
                    .imgno(walkRecommendBoardImage.getImgno())
                    .name(walkRecommendBoardImage.getName())
                    .path(walkRecommendBoardImage.getPath())
                    .uuid(walkRecommendBoardImage.getUuid())
                    .build();
        }).collect(Collectors.toList());

        boardDTO.setImageDTOList(communityBoardImageDTOList);

        return boardDTO;
    }

}
