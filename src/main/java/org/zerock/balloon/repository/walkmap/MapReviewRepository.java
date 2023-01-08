package org.zerock.balloon.repository.walkmap;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.walkmap.MapCourse;
import org.zerock.balloon.entity.walkmap.MapReview;

import javax.transaction.Transactional;
import java.util.List;

public interface MapReviewRepository extends JpaRepository<MapReview, Long> {
    @Modifying
    @Query("delete from MapReview r where r.mapCourse.mno =:mno ")
    void deleteByMno(@Param("mno") Long mno);

    @Transactional
    void deleteByMemberAndMapCourse(Member member, MapCourse mapCourse);

    @Query(value = "select r from MapReview r where r.mapCourse.mno =:mno order by r.rno desc ")
    List<MapReview> getReviewByMno(@Param("mno") Long mno);
}
