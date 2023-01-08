package org.zerock.balloon.repository.recom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.repository.recom.search.RecomSearchBoardRepository;

import java.util.List;

public interface RecomBoardRepository extends JpaRepository<RecomBoard, Long>, RecomSearchBoardRepository {

    @Query("SELECT b, w, count(r) " +
            " FROM RecomBoard b LEFT JOIN b.member w " +
            " LEFT OUTER JOIN RecomBoardReply r ON r.recomBoard = b" +
            " WHERE b.bno = :bno")
    List<Object[]> getBoardByBno( @Param("bno") Long bno); //상세보기

    @Modifying
    @Query("update RecomBoard b set b.hits = b.hits + 1 where b.bno = :bno")
    void updateHits(Long bno);

    @Query("SELECT b, mi, w, count(r) " +
            " FROM RecomBoard b LEFT JOIN b.member w " +
            " LEFT OUTER JOIN RecomBoardImage mi ON mi.recomBoard = b" +
            " LEFT OUTER JOIN RecomBoardReply r ON r.recomBoard = b" +
            " WHERE b.bno = :bno group by mi")
    List<Object[]> getBoardByBnoWithImg( @Param("bno") Long bno); //상세보기

    @Query(value = "SELECT recom_board.bno " +
            " FROM recom_board " +
            " order BY recom_board.bno DESC LIMIT 5", nativeQuery = true)
    List<Long[]> getBoardByBnoTop();

    //id로 게시판 번호 불러오기
    @Query("SELECT b.bno " +
            " FROM RecomBoard b " +
            " WHERE b.member.id = :id ")
    List<Long> getBnoById(@Param("id") String id);

    @Query("SELECT b.member.nickname " +
            " FROM RecomBoard b " +
            " WHERE b.bno = :bno ")
    String getNicknameByBno(@Param("bno") Long bno);
}
