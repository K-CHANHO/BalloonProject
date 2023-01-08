package org.zerock.balloon.service.recom;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.recom.RecomBoardReplyDTO;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.entity.recom.RecomBoardReply;
import org.zerock.balloon.repository.recom.RecomBoardReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecomBoardReplyServiceImpl implements RecomBoardReplyService {
    @Autowired
    RecomBoardReplyRepository recomBoardReplyRepository;

    @Override
    public void register(RecomBoardReplyDTO recomBoardReplyDTO) {
        RecomBoardReply recomBoardReply = dtoToEntity(recomBoardReplyDTO);

        recomBoardReplyRepository.save(recomBoardReply);

    }

    @Override
    public List<RecomBoardReplyDTO> getList(Long bno) {
        List<RecomBoardReply> result = recomBoardReplyRepository.getRepliesByRecomBoardOrderByRno(RecomBoard.builder().bno(bno).build());

        return result.stream().map(walkRecommendBoardReply -> entityToDTO(walkRecommendBoardReply)).collect(Collectors.toList());

    }



    @Override
    public void modify(RecomBoardReplyDTO recomBoardReplyDTO) {

        RecomBoardReply communityBoardReply = dtoToEntity(recomBoardReplyDTO);

        recomBoardReplyRepository.save(communityBoardReply);

    }

    @Override
    public void remove(Long rno) {

        recomBoardReplyRepository.deleteById(rno);

    }
}
