package org.zerock.balloon.service.free;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.entity.free.FreeBoardGood;
import org.zerock.balloon.repository.free.FreeBoardGoodRepository;
import org.zerock.balloon.service.free.FreeBoardGoodService;
import org.zerock.balloon.service.free.FreeBoardGoodService;


import java.math.BigInteger;
import java.util.*;

@Log4j2
@Service
public class FreeBoardGoodServiceImpl implements FreeBoardGoodService {
    @Autowired
    FreeBoardGoodRepository freeBoardGoodRepository;

    @Override
    public void good(String id, Long bno) {

        Map<String, Object> entityMap = fkToEntity(id, bno);
//        Member member = (Member)entityMap.get("member"); //비교값 member가져오기
//        CommunityBoard communityBoard = (CommunityBoard)entityMap.get("communityBoard"); //비교값 communityboard가져오기
        FreeBoardGood freeBoardGood = (FreeBoardGood) entityMap.get("freeBoardGood"); //insert값 가져오기

//        boolean result = freeBoardGoodRepository.existsByMemberAndFreeBoard(member,freeBoard); //생성 및 삭제를 구분하기 위한 조회
        boolean result = freeBoardGoodRepository.existsByMemberIdAndFreeBoardBno(id, bno); //생성 및 삭제를 구분하기 위한 조회

        System.out.println(result);

        if(result == true){ //exists는 데이터가 있으면 true, 없으면 false반환
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//            communityBoardGoodRepository.deleteByMemberAndCommunityBoard(member, communityBoard); //good데이터 삭제. delete
           freeBoardGoodRepository.deleteByMemberIdAndFreeBoardBno(id, bno); //good데이터 삭제. delete
            System.out.println("#####################################");

        }else{
           freeBoardGoodRepository.save(freeBoardGood); //good데이터 생성. insert
        }
    }

    @Override
    public Map<String, Object> goodsearch(String id, Long bno) {
        Map<String, Object> result = new HashMap<>();

        if(id != null){
            Map<String, Object> entityMap = fkToEntity(id, bno);
            Member member = (Member)entityMap.get("member"); //비교값 member만들기
            FreeBoard freeBoard = (FreeBoard) entityMap.get("freeBoard"); //비교값 recomboard만들기
            boolean torf = freeBoardGoodRepository.existsByMemberAndFreeBoard(member, freeBoard); //생성 및 삭제를 구분하기 위한 조회
            Long gcount = freeBoardGoodRepository.countByFreeBoard(freeBoard); //추천 수 조회
            result.put("torf", torf);
            result.put("gcount", gcount);

            return result;
        } else{
            Long gcount = freeBoardGoodRepository.countByFreeBoardBno(bno); //추천 수 조회
            result.put("gcount", gcount);
            return result;
        }

    }
}

