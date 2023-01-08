package org.zerock.balloon.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardGood;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.repository.community.CommunityBoardGoodRepository;

import java.util.stream.IntStream;

@SpringBootTest
public class CommunityBoardGoodRepositoryTest {
    @Autowired
    private CommunityBoardGoodRepository communityBoardGoodRepository;

    @DisplayName("테스트 좋아요 삽입")
    @Test
    public void insertGood(){
        IntStream.rangeClosed(1,50).forEach(i->{
            long bno  = (long)(Math.random() * 20) + 1;

            CommunityBoard communityBoard = CommunityBoard.builder().bno(bno).build();
            Member member = Member.builder().id("id"+bno).build();

            CommunityBoardGood communityBoardGood = CommunityBoardGood.builder()
                    .communityBoard(communityBoard)
                    .member(member)
                    .build();
            communityBoardGoodRepository.save(communityBoardGood);
        });
    }
}
