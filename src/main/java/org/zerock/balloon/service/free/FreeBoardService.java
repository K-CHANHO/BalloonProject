package org.zerock.balloon.service.free;

import org.zerock.balloon.dto.free.FreeBoardDTO;
import org.zerock.balloon.dto.free.FreeBoardImageDTO;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.PageResultDTO;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.entity.free.FreeBoardImage;
import org.zerock.balloon.entity.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public interface FreeBoardService {
    PageResultDTO<FreeBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); // 게시판 목록

    List<FreeBoardDTO> getTopList(); // 게시판 상위 목록

    List<FreeBoardDTO> getTopFive();  //5개출력

    Long register(FreeBoardDTO freeBoardDTO); // 게시물 등록

    FreeBoardDTO get(Long bno); // 게시물 상세보기


    void modify(FreeBoardDTO freeBoardDTO); // 게시물 수정

    void remove(Long bno);

    void updateHits(Long bno);

    default Map<String, Object> dtoToEntity(FreeBoardDTO freeBoardDTO){

        Map<String, Object> entityMap = new HashMap<>();

        Member member = Member.builder()
                .id(freeBoardDTO.getId())
                .build();

        FreeBoard freeBoard = FreeBoard.builder()
                .bno(freeBoardDTO.getBno())
                .subject(freeBoardDTO.getSubject())
                .title(freeBoardDTO.getTitle())
                .content(freeBoardDTO.getContent())
                .member(member)
                .hits(freeBoardDTO.getHits())
                .good(freeBoardDTO.getGood())
                .build();
        entityMap.put("board", freeBoard);

        List<FreeBoardImageDTO> imageDTOList = freeBoardDTO.getImageDTOList();
        if(imageDTOList != null && imageDTOList.size() > 0){
            List<FreeBoardImage> freeBoardImageList = imageDTOList.stream().map(communityBoardImageDTO -> {
                FreeBoardImage freeBoardImage = FreeBoardImage.builder()
                        .path(communityBoardImageDTO.getPath())
                        .name(communityBoardImageDTO.getName())
                        .uuid(communityBoardImageDTO.getUuid())
                        .freeBoard(freeBoard)
                        .build();
                return freeBoardImage;
            }).collect(Collectors.toList());
            entityMap.put("imgList", freeBoardImageList);
        }

        return entityMap;

    }

    default FreeBoardDTO entityToDTO(FreeBoard board, Member member, Long replyCount){

        FreeBoardDTO boardDTO = FreeBoardDTO.builder()
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

    default FreeBoardDTO entityToDTOWithImg(FreeBoard board, Member member, List<FreeBoardImage> Images, Long replyCount){

        FreeBoardDTO boardDTO = FreeBoardDTO.builder()
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

        List<FreeBoardImageDTO> freeBoardImageDTOList =Images.stream().map(walkfreeBoardImage -> {
            return FreeBoardImageDTO.builder()
                    .imgno(walkfreeBoardImage.getImgno())
                    .name(walkfreeBoardImage.getName())
                    .path(walkfreeBoardImage.getPath())
                    .uuid(walkfreeBoardImage.getUuid())
                    .build();
        }).collect(Collectors.toList());

        boardDTO.setImageDTOList(freeBoardImageDTOList);

        return boardDTO;
    }

}
