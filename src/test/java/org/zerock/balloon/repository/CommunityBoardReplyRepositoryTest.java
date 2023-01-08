package org.zerock.balloon.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardReply;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.repository.community.CommunityBoardReplyRepository;

import java.util.stream.IntStream;

@SpringBootTest
class CommunityBoardReplyRepositoryTest {
    @Autowired
    private CommunityBoardReplyRepository communityBoardReplyRepository;

    @DisplayName("테스트 댓글 삽입")
    @Test
    public void insertReply() {

        IntStream.rangeClosed(1, 500).forEach(i -> {

            long bno  = (long)(Math.random() * 150) + 1;
            CommunityBoard communityBoard = CommunityBoard.builder().bno(bno).build();

            long bno2 = (long)(Math.random() * 200) + 1;
            Member member = Member.builder().id("id"+bno2).nickname("nickname"+bno2).build();

            CommunityBoardReply communityBoardReply = CommunityBoardReply.builder()
                    .content("Reply......." +i)
                    .communityBoard(communityBoard)
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .build();

            communityBoardReplyRepository.save(communityBoardReply);

        });

    }
}