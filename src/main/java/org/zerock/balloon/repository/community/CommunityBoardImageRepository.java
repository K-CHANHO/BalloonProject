package org.zerock.balloon.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.community.CommunityBoardImage;


public interface CommunityBoardImageRepository extends JpaRepository<CommunityBoardImage, Long> {

    @Transactional
    @Modifying
    @Query("delete from CommunityBoardImage mi where mi.communityBoard.bno = :bno")
    void deleteByBno(@Param("bno") Long bno); // 게시물의 댓글 삭제
}
