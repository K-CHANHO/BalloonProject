package org.zerock.balloon.controller.community;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.balloon.service.community.CommunityBoardGoodService;

import java.util.Map;

@RestController
@Log4j2
public class CommunityBoardGoodController {

    @Autowired
    private CommunityBoardGoodService communityBoardGoodService;

    @GetMapping("/community/good") //데이터 생성 및 삭제
    public void good(String id, long bno){
        communityBoardGoodService.good(id, bno);
    }

    @GetMapping("/community/goodcount") //추천 수 조회 및 데이터 존재여부 확인
    public Map<String, Object> goodsearch(String id, long bno){
        Map<String, Object> goods = communityBoardGoodService.goodsearch(id, bno);
        return goods;
    }
}
