package org.zerock.balloon.service.free;

import org.zerock.balloon.dto.free.FreeBoardGoodDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.entity.free.FreeBoardGood;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FreeBoardGoodService {

    void good(String id, Long bno);

    Map<String, Object> goodsearch(String id, Long bno);

    default Map<String, Object> fkToEntity(String id, Long bno){ //추천 수 조회, 데이터 존재여부, 생성 및 삭제 다 사용
        Map<String, Object> entityMap = new HashMap<>();
        Member member = Member.builder().id(id).build();
        FreeBoard freeBoard = FreeBoard.builder().bno(bno).build();
        FreeBoardGood freeBoardGood = FreeBoardGood.builder()
                .member(member)
                .freeBoard(freeBoard)
                .build();
        entityMap.put("member", member); //FK값 만들기(id)
        entityMap.put("freeBoard", freeBoard); //FK값 만들기(bno)
        entityMap.put("freeBoardGood", freeBoardGood); //추천 entity만들기
        return entityMap;
    }

}
