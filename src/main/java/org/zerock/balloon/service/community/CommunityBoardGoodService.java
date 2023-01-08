package org.zerock.balloon.service.community;

import org.zerock.balloon.dto.community.CommunityBoardGoodDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardGood;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CommunityBoardGoodService {

    void good(String id, Long bno);
    
    Map<String, Object> goodsearch(String id, Long bno);

    default Map<String, Object> fkToEntity(String id, Long bno){ //추천 수 조회, 데이터 존재여부, 생성 및 삭제 다 사용
        Map<String, Object> entityMap = new HashMap<>();
        Member member = Member.builder().id(id).build();
        CommunityBoard communityBoard = CommunityBoard.builder().bno(bno).build();
        CommunityBoardGood communityBoardGood = CommunityBoardGood.builder()
                .member(member)
                .communityBoard(communityBoard)
                .build();
        entityMap.put("member", member); //FK값 만들기(id)
        entityMap.put("communityBoard", communityBoard); //FK값 만들기(bno)
        entityMap.put("communityBoardGood", communityBoardGood); //추천 entity만들기
        return entityMap;
    }

}
