package org.zerock.balloon.repository.recom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.entity.recom.RecomBoardReply;

import java.util.List;

public interface RecomBoardReplyRepository extends JpaRepository<RecomBoardReply, Long> {

    @Transactional
    @Modifying
    @Query("delete from RecomBoardReply r where r.recomBoard.bno = :bno")
    void deleteByBno(@Param("bno") Long bno); // 게시물의 댓글 삭제

    List<RecomBoardReply> getRepliesByRecomBoardOrderByRno(RecomBoard recomBoard); // 게시물 별 댓글 목록

    @Query(value = " UPDATE recom_board_reply " +
            " SET nickname = :nickname " +
            " WHERE id = :id", nativeQuery = true)
    void updateNicknameById(@Param("id")String id, @Param("nickname")String nickname);
}
