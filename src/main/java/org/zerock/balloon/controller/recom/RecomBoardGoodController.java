package org.zerock.balloon.controller.recom;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.balloon.dto.recom.RecomBoardGoodDTO;
import org.zerock.balloon.service.recom.RecomBoardGoodService;
import org.zerock.balloon.service.recom.RecomBoardService;

import java.util.Map;

@RestController
@Log4j2
public class RecomBoardGoodController {
    @Autowired
    private RecomBoardGoodService recomBoardGoodService;
    @GetMapping("/recom/good") //데이터 생성 및 삭제
    public void good(String id, long bno){
        recomBoardGoodService.good(id, bno);
    }

    @GetMapping("/recom/goodcount") //추천 수 조회 및 데이터 존재여부 확인
    public Map<String, Object> goodsearch(String id, long bno){
        Map<String, Object> goods = recomBoardGoodService.goodsearch(id, bno);
        return goods;
    }
}
