package org.zerock.balloon.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.repository.community.CommunityBoardRepository;

import java.util.stream.IntStream;

@SpringBootTest
class CommunityBoardRepositoryTest {
    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    @DisplayName("테스트 게시물 삽입")
    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1,150).forEach(i->{
            Member member= Member.builder().id("id"+i).build();

            CommunityBoard communityBoard = CommunityBoard.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .member(member)
                    .build();
            communityBoardRepository.save(communityBoard);
        });
    }
}