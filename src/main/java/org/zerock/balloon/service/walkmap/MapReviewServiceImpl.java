package org.zerock.balloon.service.walkmap;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.community.CommunityBoardReplyDTO;
import org.zerock.balloon.dto.walkmap.MapReviewDTO;
import org.zerock.balloon.entity.community.CommunityBoardReply;
import org.zerock.balloon.entity.walkmap.MapReview;
import org.zerock.balloon.repository.walkmap.MapReviewRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapReviewServiceImpl implements MapReviewService{
    @Autowired
    MapReviewRepository mapReviewRepository;
    @Override
    public List<MapReviewDTO> getReview(Long mno){
        List<MapReview> mapReviews = mapReviewRepository.getReviewByMno(mno);
        System.out.println(mapReviews +"{}{}{{}{}{}{}");
        List<MapReviewDTO> result = new ArrayList<>();
        mapReviews.forEach(arr -> {
            result.add(entityToDTO(arr));
        });
        return result;
    }
    @Override
    public void register(MapReviewDTO mapReviewDTO){
        MapReview mapReview = dtoToEntity(mapReviewDTO);
        mapReviewRepository.save(mapReview);
    }

    @Override
    public void modify(MapReviewDTO mapReviewDTO) {
        MapReview mapReview = dtoToEntity(mapReviewDTO);
        mapReviewRepository.save(mapReview);
    }

    @Override
    public void remove(Long rno) {

        mapReviewRepository.deleteById(rno);

    }
}