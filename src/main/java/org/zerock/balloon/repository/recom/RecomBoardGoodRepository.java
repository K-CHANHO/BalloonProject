package org.zerock.balloon.repository.recom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.entity.recom.RecomBoardGood;

import java.util.List;
import java.util.Optional;

public interface RecomBoardGoodRepository extends JpaRepository<RecomBoardGood, Long>{
    boolean existsByMemberAndRecomBoard(Member member, RecomBoard recomBoard); //참조값 id랑 bno로 데이터 존재여부 확인
    @Transactional
    void deleteByMemberAndRecomBoard(Member member, RecomBoard recomBoard); //참조값 id랑 bno로 데이터 삭제
    Long countByRecomBoard(RecomBoard recomBoard); //해당 글 추천 수 조회(참조값 bno사용)
    Long countByRecomBoardBno(Long bno);

    @Query(value = "SELECT recom_board_bno, COUNT(count) " +
            " FROM recom_board_good r " +
            " group BY r.recom_board_bno asc " +
            " order BY COUNT(count) DESC LIMIT 3", nativeQuery = true)
    List<Long[]> getBoardByGoodCount();

    @Transactional
    @Modifying
    @Query("delete from RecomBoardGood bg where bg.recomBoard.bno = :bno")
    void deleteByBno(@Param("bno") Long bno); // 게시물의 댓글 삭제


    //멤버가 누른 좋아요 삭제
    @Transactional
    @Modifying
    @Query("delete from RecomBoardGood bg where bg.member.id = :id")
    void deleteById(@Param("id") String id);



}
