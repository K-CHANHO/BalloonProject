package org.zerock.balloon.service.walkmap;

import org.zerock.balloon.dto.walkmap.MapReviewDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.walkmap.MapCourse;
import org.zerock.balloon.entity.walkmap.MapReview;

import java.util.List;

public interface MapReviewService {
    List<MapReviewDTO> getReview(Long mno);

    void register(MapReviewDTO mapReviewDTO);

    void modify(MapReviewDTO mapReviewDTO);

    void remove(Long rno); // 댓글 삭제

    default MapReview dtoToEntity(MapReviewDTO mapReviewDTO) {

        MapCourse mapCourse = MapCourse.builder().mno(mapReviewDTO.getMno()).build();
        Member member = Member.builder().id(mapReviewDTO.getId()).build();
        MapReview mapReview = MapReview.builder()
                .rno(mapReviewDTO.getRno())
                .content(mapReviewDTO.getContent())
                .member(member)
                .mapCourse(mapCourse)
                .build();

        return mapReview;
    }
    default MapReviewDTO entityToDTO(MapReview mapReview){
        MapReviewDTO dto = MapReviewDTO.builder()
                .rno(mapReview.getRno())
                .mno(mapReview.getMapCourse().getMno())
                .id(mapReview.getMember().getId())
                .content(mapReview.getContent())
                .nickname(mapReview.getMember().getNickname())
                .regDate(mapReview.getRegDate())
                .modDate(mapReview.getModDate())
                .build();

        return dto;
    }
}