package org.zerock.balloon.repository.qna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.balloon.entity.qna.QnaBoard;
import org.zerock.balloon.repository.qna.search.QnaSearchBoardRepository;
//import org.zerock.balloon.repository.search.SearchBoardRepository;

import java.util.List;

public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long>, QnaSearchBoardRepository {

    @Query(value ="SELECT b, m, count(r) " +
            " FROM QnaBoard b " +
            " LEFT JOIN b.member m " +
            " LEFT JOIN QnaReply r ON r.qnaBoard = b " +
            " GROUP BY b",
            countQuery ="SELECT count(b) FROM QnaBoard b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("SELECT b, r FROM QnaReply r LEFT JOIN r.qnaBoard b WHERE b.qbno = :qbno")
    List<Object[]> getBoardWithReply(@Param("qbno") Long qbno);

    @Query("SELECT b, w, count(r) " +
            " FROM QnaBoard b LEFT JOIN b.member w " +
            " LEFT OUTER JOIN QnaReply r ON r.qnaBoard = b" +
            " WHERE b.qbno = :qbno")
    List<Object[]> getBoardByBno( @Param("qbno") Long qbno); //상세보기

    @Modifying
    @Query("update QnaBoard b set b.hits = b.hits + 1 where b.qbno = :qbno")
    void updateHits(Long qbno);

    @Query(value = "SELECT qna_board.qbno " +
            " FROM qna_board " +
            " order BY qna_board.qbno DESC LIMIT 5", nativeQuery = true)
    List<Long[]> getBoardByBnoTop();

    @Query(value = " UPDATE qna_board_reply " +
            " SET nickname = :nickname " +
            " WHERE id = :id", nativeQuery = true)
    void updateNicknameById(@Param("id")String id, @Param("nickname")String nickname);

    //id로 게시판 번호 불러오기
    @Query("SELECT b.qbno " +
            " FROM QnaBoard b " +
            " WHERE b.member.id = :id ")
    List<Long> getBnoById(@Param("id") String id);
}
