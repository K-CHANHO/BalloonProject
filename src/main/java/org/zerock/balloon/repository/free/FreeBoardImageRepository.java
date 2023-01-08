package org.zerock.balloon.repository.free;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.balloon.entity.free.FreeBoardImage;

public interface FreeBoardImageRepository extends JpaRepository<FreeBoardImage, Long> {

    @Transactional
    @Modifying
    @Query("delete from FreeBoardImage mi where mi.freeBoard.bno = :bno")
    void deleteByBno(@Param("bno") Long bno);
}
