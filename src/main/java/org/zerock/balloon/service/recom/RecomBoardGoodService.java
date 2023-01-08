package org.zerock.balloon.service.recom;

import org.zerock.balloon.dto.recom.RecomBoardImageDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.entity.recom.RecomBoardGood;
import org.zerock.balloon.entity.recom.RecomBoardImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface RecomBoardGoodService {

    void good(String id, Long bno);

    Map<String, Object> getGood();

    Map<String, Object> goodsearch(String id, Long bno);

    default Map<String, Object> fkToEntity(String id, Long bno){ //추천 수 조회, 데이터 존재여부, 생성 및 삭제 다 사용
        Map<String, Object> entityMap = new HashMap<>();
        Member member = Member.builder().id(id).build();
        RecomBoard recomBoard = RecomBoard.builder().bno(bno).build();
        RecomBoardGood recomBoardGood = RecomBoardGood.builder()
                .member(member)
                .recomBoard(recomBoard)
                .build();
        entityMap.put("member", member); //FK값 만들기(id)
        entityMap.put("recomBoard", recomBoard); //FK값 만들기(bno)
        entityMap.put("recomBoardGood", recomBoardGood); //추천 entity만들기
        return entityMap;
    }
    default List<RecomBoardImageDTO> entityToImgDTO(List<RecomBoardImage> recomBoardImages){
        List<RecomBoardImageDTO> imglist =recomBoardImages.stream().map(recomBoardImage -> {
            return RecomBoardImageDTO.builder().imgname(recomBoardImage.getImgname())
                    .path(recomBoardImage.getPath())
                    .uuid(recomBoardImage.getUuid())
                    .build();
        }).collect(Collectors.toList());
        return imglist;
    }
}
