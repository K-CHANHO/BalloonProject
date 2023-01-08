package org.zerock.balloon.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.dto.community.CommunityBoardReplyDTO;
import org.zerock.balloon.repository.community.CommunityBoardGoodRepository;
import org.zerock.balloon.repository.community.CommunityBoardRepository;
import org.zerock.balloon.service.community.CommunityBoardReplyService;
import org.zerock.balloon.service.community.CommunityBoardService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CommunityBoardServiceTest {

    @Autowired
    private CommunityBoardService communityBoardService;
    @Autowired
    private CommunityBoardReplyService communityBoardReplyService;
    @Autowired
    private CommunityBoardGoodRepository communityBoardGoodRepository;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    @Test
    public void testGetList(){
        Long bno = 20L;

        List<CommunityBoardReplyDTO> replyDTOList = communityBoardReplyService.getList(bno);

        replyDTOList.forEach(replyDTO -> System.out.println(replyDTO));
    }

    @Test
    public void testGetTopList(){
        List<Long> bnos = communityBoardGoodRepository.getBoardByGoodCount();
        System.out.println("bnos = " + bnos);

        List<CommunityBoardDTO> result = communityBoardService.getTopList();
        System.out.println("result = " + result);
    }


}