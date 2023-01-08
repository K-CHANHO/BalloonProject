package org.zerock.balloon.repository.qna;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.qna.QnaBoard;
import org.zerock.balloon.repository.community.CommunityBoardRepository;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QnaBoardRepositoryTest {

    @Autowired
    private QnaBoardRepository qnaBoardRepository;

    @DisplayName("테스트 게시물 삽입")
    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1,150).forEach(i->{
            Member member= Member.builder().id("id"+i).build();

            QnaBoard qnaBoard = QnaBoard.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .member(member)
                    .build();
            qnaBoardRepository.save(qnaBoard);
        });
    }

}