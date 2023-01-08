package org.zerock.balloon.service.recom;

import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.PageResultDTO;
import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.dto.recom.RecomBoardDTO;
import org.zerock.balloon.dto.recom.RecomBoardImageDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.entity.recom.RecomBoardImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface RecomBoardService {
    RecomBoardDTO get(Long bno); //상세보기
    Long register(RecomBoardDTO recomBoardDTO); //등록
    List<RecomBoardDTO> getTopList(); // 게시판 상위 목록
    List<RecomBoardDTO> getTopFive();

    void removeWithReplies(Long bno);//삭제

    void updateHits(Long bno);

    void modify(RecomBoardDTO boardDTO);//수정
    PageResultDTO<RecomBoardDTO,Object[]> getList(PageRequestDTO pageRequestDTO); //목록

    default Map<String, Object> dtoToEntity(RecomBoardDTO recomdto){
        Map<String, Object> entityMap = new HashMap<>();
        Member member= Member.builder().id(recomdto.getId()).build();
        RecomBoard board= RecomBoard.builder()
                .bno(recomdto.getBno())
                .subject(recomdto.getSubject())
                .title(recomdto.getTitle())
                .content(recomdto.getContent())
                .member(member)
                .good(recomdto.getGood())
                .hits(recomdto.getHits())
                .hate(recomdto.getHate())
                .build();

        entityMap.put("board", board);

        List<RecomBoardImageDTO> imageDTOList = recomdto.getImageDTOList();

        if(imageDTOList != null && imageDTOList.size() > 0){
            List<RecomBoardImage> recomBoardImageList = imageDTOList.stream().map(recomBoardImageDTO -> {
                RecomBoardImage recomBoardImage = RecomBoardImage.builder()
                        .path(recomBoardImageDTO.getPath())
                        .imgname(recomBoardImageDTO.getImgname())
                        .uuid(recomBoardImageDTO.getUuid())
                        .recomBoard(board)
                        .build();
                return recomBoardImage;
            }).collect(Collectors.toList());
            entityMap.put("imgList", recomBoardImageList);
        }
        return entityMap;
    }
    default RecomBoardDTO entityToDTOWithImg(RecomBoard recomboard, List<RecomBoardImage> recomBoardImages,Member member,Long replyCount){
        RecomBoardDTO boardDTO=RecomBoardDTO.builder()
                .bno(recomboard.getBno())
                .subject(recomboard.getSubject())
                .title(recomboard.getTitle())
                .content(recomboard.getContent())
                .regDate(recomboard.getRegDate())
                .modDate(recomboard.getModDate())
                .nickname(member.getNickname())
                .id(member.getId())
                .replyCount(replyCount.intValue())
                .hits(recomboard.getHits())
                .good(recomboard.getGood())
                .hate(recomboard.getHate())
                .build();
        List<RecomBoardImageDTO> recomBoardImageDTOList =recomBoardImages.stream().map(recomBoardImage -> {
            return RecomBoardImageDTO.builder()
                    .imgno(recomBoardImage.getImgno())
                    .imgname(recomBoardImage.getImgname())
                    .path(recomBoardImage.getPath())
                    .uuid(recomBoardImage.getUuid())
                    .build();
        }).collect(Collectors.toList());

        boardDTO.setImageDTOList(recomBoardImageDTOList);

        return boardDTO;
    }
    default RecomBoardDTO entityToDTO(RecomBoard recomboard, Member member,Long replyCount){
        RecomBoardDTO boardDTO=RecomBoardDTO.builder()
                .bno(recomboard.getBno())
                .subject(recomboard.getSubject())
                .title(recomboard.getTitle())
                .content(recomboard.getContent())
                .regDate(recomboard.getRegDate())
                .modDate(recomboard.getModDate())
                .nickname(member.getNickname())
                .id(member.getId())
                .replyCount(replyCount.intValue())
                .hits(recomboard.getHits())
                .good(recomboard.getGood())
                .hate(recomboard.getHate())
                .build();

        return boardDTO;
    }
}
