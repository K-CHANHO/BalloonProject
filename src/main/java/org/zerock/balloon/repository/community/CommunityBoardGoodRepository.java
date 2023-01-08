package org.zerock.balloon.repository.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardGood;

import java.util.List;
import java.util.Optional;

public interface CommunityBoardGoodRepository extends JpaRepository<CommunityBoardGood, Long> {

    boolean existsByMemberAndCommunityBoard(Member member, CommunityBoard communityBoard); //참조값 id랑 bno로 데이터 존재여부 확인

    boolean existsByMemberIdAndCommunityBoardBno(String id, Long bno); //참조값 id랑 bno로 데이터 존재여부 확인


//    @Transactional
//    void deleteByMemberAndCommunityBoard(Member member, CommunityBoard communityBoard); //참조값 id랑 bno로 데이터 삭제

    @Transactional
    void deleteByMemberIdAndCommunityBoardBno(String id, Long bno); //참조값 id랑 bno로 데이터 삭제

    @Transactional
    @Modifying
    @Query("delete from CommunityBoardGood bg where bg.communityBoard.bno = :bno")
    void deleteByBno(@Param("bno") Long bno); // 게시물의 댓글 삭제

    Long countByCommunityBoard(CommunityBoard communityBoard); //해당 글 추천 수 조회(참조값 bno사용)
    Long countByCommunityBoardBno(Long bno); // 비회원일 경우 좋아요 수 조회

    @Query(value = "SELECT community_board_bno " +
            " FROM community_board_good r " +
            " group BY r.community_board_bno asc " +
            " order BY COUNT(count) DESC LIMIT 3", nativeQuery = true)
    List<Long> getBoardByGoodCount();

    //멤버가 누른 좋아요 삭제
    @Transactional
    @Modifying
    @Query("delete from RecomBoardGood bg where bg.member.id = :id")
    void deleteById(@Param("id") String id);

}
