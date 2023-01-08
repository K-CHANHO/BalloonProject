package org.zerock.balloon.service.recom;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.recom.RecomBoardGoodDTO;
import org.zerock.balloon.dto.recom.RecomBoardImageDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.entity.recom.RecomBoardGood;
import org.zerock.balloon.entity.recom.RecomBoardImage;
import org.zerock.balloon.repository.recom.RecomBoardGoodRepository;
import org.zerock.balloon.repository.recom.RecomBoardImageRepository;
import org.zerock.balloon.repository.recom.RecomBoardRepository;

import java.math.BigInteger;
import java.util.*;

@Log4j2
@Service
public class RecomBoardGoodServiceImpl implements RecomBoardGoodService{
    @Autowired
    RecomBoardRepository recomBoardRepository;
    @Autowired
    RecomBoardGoodRepository recomBoardGoodRepository;
    @Autowired
    RecomBoardImageRepository recomBoardImageRepository;

    @Override
    public void good(String id, Long bno) {
        Map<String, Object> entityMap = fkToEntity(id, bno);
        Member member = (Member)entityMap.get("member"); //비교값 member가져오기
        RecomBoard recomBoard = (RecomBoard)entityMap.get("recomBoard"); //비교값 recomboard가져오기
        RecomBoardGood recomBoardGood = (RecomBoardGood)entityMap.get("recomBoardGood"); //insert값 가져오기
        boolean result = recomBoardGoodRepository.existsByMemberAndRecomBoard(member, recomBoard); //생성 및 삭제를 구분하기 위한 조회
        if(result == true){ //exists는 데이터가 있으면 true, 없으면 false반환
            recomBoardGoodRepository.deleteByMemberAndRecomBoard(member, recomBoard); //good데이터 삭제. delete
        }else{
            recomBoardGoodRepository.save(recomBoardGood); //good데이터 생성. insert
        }
    }

    @Override
    public Map<String, Object> getGood() {
        Map<String, Object> result = new HashMap<>();
        List<Long[]> goodboard = recomBoardGoodRepository.getBoardByGoodCount();
        List<RecomBoardImage> recomBoardImageDTOS = new ArrayList<>();
        List<Long> countgrade = new ArrayList<>();
        List<Long> boardbno = new ArrayList<>();
        List<String> mnickname = new ArrayList<>();
        goodboard.forEach(arr -> {
            RecomBoardImage img = recomBoardImageRepository.getCountByBoardImg(arr[0]);
            String nickname = recomBoardRepository.getNicknameByBno(arr[0]);
            boardbno.add(arr[0]);
            countgrade.add(arr[1]);
            recomBoardImageDTOS.add(img);
            mnickname.add(nickname);
        });
        result.put("img", entityToImgDTO(recomBoardImageDTOS));
        result.put("count", countgrade);
        result.put("bno", boardbno);
        result.put("nickname", mnickname);
        return result;
    }


    @Override
    public Map<String, Object> goodsearch(String id, Long bno) {
        Map<String, Object> result = new HashMap<>();
        if(id != null){
            Map<String, Object> entityMap = fkToEntity(id, bno);
            Member member = (Member)entityMap.get("member"); //비교값 member만들기
            RecomBoard recomBoard = (RecomBoard) entityMap.get("recomBoard"); //비교값 recomboard만들기
            boolean torf = recomBoardGoodRepository.existsByMemberAndRecomBoard(member, recomBoard); //생성 및 삭제를 구분하기 위한 조회
            Long gcount = recomBoardGoodRepository.countByRecomBoard(recomBoard); //추천 수 조회
            result.put("torf", torf);
            result.put("gcount", gcount);

            return result;
        } else{
            Long gcount = recomBoardGoodRepository.countByRecomBoardBno(bno); //추천 수 조회
            result.put("gcount", gcount);
            return result;
        }

    }

}
