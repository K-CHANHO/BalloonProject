package org.zerock.balloon.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardImage;
import org.zerock.balloon.repository.community.CommunityBoardImageRepository;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
class CommunityBoardImageRepositoryTest {
    @Autowired
    private CommunityBoardImageRepository communityBoardImageRepository;

    @DisplayName("테스트 이미지 삽입")
    @Test
    public void insertImage(){
        IntStream.rangeClosed(1,20).forEach(i->{
            long bno  = (long)(Math.random() * 20) + 1;
            CommunityBoard communityBoard = CommunityBoard.builder().bno(bno).build();

            CommunityBoardImage communityBoardImage = CommunityBoardImage.builder()
                    .communityBoard(communityBoard)
                    .name("img"+i+".jpg")
                    .path("/img")
                    .uuid(UUID.randomUUID().toString())
                    .build();
            communityBoardImageRepository.save(communityBoardImage);
        });
    }
}