package org.zerock.balloon.repository.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.repository.community.search.CommunitySearchBoardRepository;

import java.util.List;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard, Long>, CommunitySearchBoardRepository {

    @Query("select b, m, count(r) " +
            "from CommunityBoard b left join b.member m " +
            "left join CommunityBoardReply r on r.communityBoard = b " +
            "where b.bno = :bno")
    List<Object[]> getBoardByBno(@Param("bno") Long bno);

    @Query("SELECT b, m, mi, count(r) " +
            " FROM CommunityBoard b LEFT JOIN b.member m " +
            " LEFT OUTER JOIN CommunityBoardImage mi ON mi.communityBoard = b" +
            " LEFT OUTER JOIN CommunityBoardReply r ON r.communityBoard = b" +
            " WHERE b.bno = :bno group by mi")
    List<Object[]> getBoardByBnoWithImg( @Param("bno") Long bno); //상세보기

    @Query(value = "select b, m, count(r) " +
            "from CommunityBoard b " +
            "left join b.member m " +
            "left join CommunityBoardReply r on r.communityBoard = b " +
            "group by b ",
            countQuery = "select count(b) from CommunityBoard b")
    Page<Object[]> getBoardList(Pageable pageable);

    @Modifying
    @Query("update CommunityBoard b set b.hits = b.hits + 1 where b.bno = :bno")
    void updateHits(Long bno);

    @Query(value = "SELECT community_board.bno " +
            " FROM community_board " +
            " order BY community_board.bno DESC LIMIT 5", nativeQuery = true)
    List<Long[]> getBoardByBnoTop();

    //id로 게시판 번호 불러오기
    @Query("SELECT b.bno " +
            " FROM CommunityBoard b " +
            " WHERE b.member.id = :id ")
    List<Long> getBnoById(@Param("id") String id);

}
