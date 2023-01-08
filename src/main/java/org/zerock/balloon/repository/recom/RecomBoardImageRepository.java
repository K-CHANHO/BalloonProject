package org.zerock.balloon.repository.recom;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.dto.recom.RecomBoardImageDTO;
import org.zerock.balloon.entity.recom.RecomBoardImage;

import java.math.BigInteger;
import java.util.List;

public interface RecomBoardImageRepository extends JpaRepository<RecomBoardImage, Long> {

    @Transactional
    @Modifying
    @Query("delete from RecomBoardImage mi where mi.recomBoard.bno=:bno")
    void deleteByBno(Long bno);

    @Query(value = "SELECT * FROM recom_board_image " +
            " WHERE recom_board_bno = :bno LIMIT 1", nativeQuery = true)
    RecomBoardImage getCountByBoardImg(@Param("bno") Long bno);
}
