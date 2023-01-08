package org.zerock.balloon.controller.free;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.balloon.service.free.FreeBoardGoodService;

import java.util.Map;

@RestController
@Log4j2
public class FreeBoardGoodController {

    @Autowired
    private FreeBoardGoodService freeBoardGoodService;

    @GetMapping("/free/good") //데이터 생성 및 삭제
    public void good(String id, long bno){
        freeBoardGoodService.good(id, bno);
    }

    @GetMapping("/free/goodcount") //추천 수 조회 및 데이터 존재여부 확인
    public Map<String, Object> goodsearch(String id, long bno){
        Map<String, Object> goods = freeBoardGoodService.goodsearch(id, bno);
        return goods;
    }
}
