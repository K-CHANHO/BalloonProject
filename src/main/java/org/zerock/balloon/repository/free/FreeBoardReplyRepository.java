package org.zerock.balloon.repository.free;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.entity.free.FreeBoardReply;

import java.util.List;

public interface FreeBoardReplyRepository extends JpaRepository<FreeBoardReply, Long> {
    //댓글삭제
    @Transactional
    @Modifying
    @Query("delete from FreeBoardReply r where r.freeBoard.bno=:bno")
    void deleteByBno(@Param("bno") Long bno);
    //댓글목록
    List<FreeBoardReply> getRepliesByFreeBoardOrderByRno(FreeBoard freeBoard);
}
