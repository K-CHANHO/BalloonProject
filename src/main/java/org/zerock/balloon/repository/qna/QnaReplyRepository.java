package org.zerock.balloon.repository.qna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.qna.QnaBoard;
import org.zerock.balloon.entity.qna.QnaReply;

import java.util.List;

public interface QnaReplyRepository extends JpaRepository<QnaReply, Long> {
    //댓글삭제
    @Transactional
    @Modifying
    @Query("delete from QnaReply r where r.qnaBoard.qbno=:qbno")
    void deleteByQbno(Long qbno);
    //댓글목록
    List<QnaReply> getQnaRepliesByQnaBoardOrderByQbrno(QnaBoard qnaBoard);

    @Query(value = " UPDATE qna_board_reply " +
            " SET nickname = :nickname " +
            " WHERE id = :id", nativeQuery = true)
    void updateNicknameById(@Param("id")String id, @Param("nickname")String nickname);
}
