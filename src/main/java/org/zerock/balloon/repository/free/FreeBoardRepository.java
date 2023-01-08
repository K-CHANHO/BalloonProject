package org.zerock.balloon.repository.free;

import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.repository.free.search.FreeSearchBoardRepository;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeSearchBoardRepository {


    @Query("SELECT b, m, count(r) " +
            " from FreeBoard b left join b.member m " +
            " left join  FreeBoardReply r on r.freeBoard = b" +
            " where b.bno = :bno")
    List<Object[]> getBoardByBno( @Param("bno") Long bno);


    @Query("SELECT b, m, mi, count(r) " +
            " FROM FreeBoard b LEFT JOIN b.member m " +
            " LEFT OUTER JOIN FreeBoardImage mi ON mi.freeBoard = b" +
            " LEFT OUTER JOIN FreeBoardReply r ON r.freeBoard = b" +
            " WHERE b.bno = :bno group by mi")
    List<Object[]> getBoardByBnoWithImg( @Param("bno") Long bno); //상세보기

    @Query(value = "select b, m, count(r) " +
             "from FreeBoard b " +
             "left join b.member m " +
             "left join FreeBoardReply r on r.freeBoard = b " +
             "group by b ",
              countQuery = "select count(b) from FreeBoard b")
    Page<Object> getBoardList(Pageable pageable);

    @Query(value = "SELECT free_board.bno " +
            " FROM free_board " +
            " order BY free_board.bno DESC LIMIT 5", nativeQuery = true)
    List<Long[]> getBoardByBnoTop();

    //id로 게시판 번호 불러오기
    @Query("SELECT b.bno " +
            " FROM FreeBoard b " +
            " WHERE b.member.id = :id ")
    List<Long> getBnoById(@Param("id") String id);

    @Modifying
    @Query("update FreeBoard b set b.hits = b.hits + 1 where b.bno = :bno")
    void updateHits(Long bno);
}
