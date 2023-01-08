package org.zerock.balloon.service.community;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardGood;
import org.zerock.balloon.repository.community.CommunityBoardGoodRepository;
import org.zerock.balloon.service.community.CommunityBoardGoodService;


import java.math.BigInteger;
import java.util.*;

@Log4j2
@Service
public class CommunityBoardGoodServiceImpl implements CommunityBoardGoodService {
    @Autowired
    CommunityBoardGoodRepository communityBoardGoodRepository;

    @Override
    public void good(String id, Long bno) {

        Map<String, Object> entityMap = fkToEntity(id, bno);
//        Member member = (Member)entityMap.get("member"); //비교값 member가져오기
//        CommunityBoard communityBoard = (CommunityBoard)entityMap.get("communityBoard"); //비교값 communityboard가져오기
        CommunityBoardGood communityBoardGood = (CommunityBoardGood) entityMap.get("communityBoardGood"); //insert값 가져오기

//        boolean result = communityBoardGoodRepository.existsByMemberAndCommunityBoard(member, communityBoard); //생성 및 삭제를 구분하기 위한 조회
        boolean result = communityBoardGoodRepository.existsByMemberIdAndCommunityBoardBno(id, bno); //생성 및 삭제를 구분하기 위한 조회

        System.out.println(result);

        if(result == true){ //exists는 데이터가 있으면 true, 없으면 false반환
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//            communityBoardGoodRepository.deleteByMemberAndCommunityBoard(member, communityBoard); //good데이터 삭제. delete
            communityBoardGoodRepository.deleteByMemberIdAndCommunityBoardBno(id, bno); //good데이터 삭제. delete
            System.out.println("#####################################");

        }else{
            communityBoardGoodRepository.save(communityBoardGood); //good데이터 생성. insert
        }
    }

    @Override
    public Map<String, Object> goodsearch(String id, Long bno) {
        Map<String, Object> result = new HashMap<>();

        if(id != null){
            Map<String, Object> entityMap = fkToEntity(id, bno);
            Member member = (Member)entityMap.get("member"); //비교값 member만들기
            CommunityBoard communityBoard = (CommunityBoard) entityMap.get("communityBoard"); //비교값 recomboard만들기
            boolean torf = communityBoardGoodRepository.existsByMemberAndCommunityBoard(member, communityBoard); //생성 및 삭제를 구분하기 위한 조회
            Long gcount = communityBoardGoodRepository.countByCommunityBoard(communityBoard); //추천 수 조회
            result.put("torf", torf);
            result.put("gcount", gcount);

            return result;
        } else{
            Long gcount = communityBoardGoodRepository.countByCommunityBoardBno(bno); //추천 수 조회
            result.put("gcount", gcount);
            return result;
        }

    }
}
