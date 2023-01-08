package org.zerock.balloon.repository.free;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.entity.free.FreeBoardGood;

import java.util.List;

public interface FreeBoardGoodRepository extends JpaRepository<FreeBoardGood, Long> {

    boolean existsByMemberAndFreeBoard(Member member, FreeBoard freeBoard);

    boolean existsByMemberIdAndFreeBoardBno(String id, Long bno);


    @Transactional
    void deleteByMemberIdAndFreeBoardBno(String id, Long bno);

    @Transactional
    @Modifying
    @Query("delete from FreeBoardGood bg where bg.freeBoard.bno = :bno")
    void deleteByBno(@Param("bno") Long bno);


    Long countByFreeBoard(FreeBoard freeBoard);

    Long countByFreeBoardBno(Long bno);

    @Query(value = "SELECT free_board_bno " +
            " FROM free_board_good r " +
            " group BY r.free_board_bno asc " +
            " order BY COUNT(count) DESC LIMIT 3", nativeQuery = true)
    List<Long> getBoardByGoodCount();

}
