package org.zerock.balloon.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardReply;

import java.util.List;

public interface CommunityBoardReplyRepository extends JpaRepository<CommunityBoardReply, Long> {

    @Transactional
    @Modifying
    @Query("delete from CommunityBoardReply r where r.communityBoard.bno = :bno")
    void deleteByBno(@Param("bno") Long bno); // 게시물의 댓글 삭제

    List<CommunityBoardReply> getRepliesByCommunityBoardOrderByRno(CommunityBoard communityBoard); // 게시물 별 댓글 목록

    @Query(value = " UPDATE community_board_reply " +
            " SET nickname = :nickname " +
            " WHERE id = :id", nativeQuery = true)
    void updateNicknameById(@Param("id")String id, @Param("nickname")String nickname);

}
