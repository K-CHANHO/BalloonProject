package org.zerock.balloon.service.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.community.CommunityBoardReplyDTO;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardReply;
import org.zerock.balloon.repository.community.CommunityBoardReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityBoardReplyServiceImpl implements CommunityBoardReplyService {

    @Autowired
    CommunityBoardReplyRepository communityBoardReplyRepository;

    @Override
    public void register(CommunityBoardReplyDTO communityBoardReplyDTO) {
        CommunityBoardReply communityBoardReply = dtoToEntity(communityBoardReplyDTO);

        communityBoardReplyRepository.save(communityBoardReply);

    }

    @Override
    public List<CommunityBoardReplyDTO> getList(Long bno) {
        List<CommunityBoardReply> result = communityBoardReplyRepository.getRepliesByCommunityBoardOrderByRno(CommunityBoard.builder().bno(bno).build());

        return result.stream().map(walkRecommendBoardReply -> entityToDTO(walkRecommendBoardReply)).collect(Collectors.toList());

    }

    @Override
    public void modify(CommunityBoardReplyDTO communityBoardReplyDTO) {

        CommunityBoardReply communityBoardReply = dtoToEntity(communityBoardReplyDTO);

        communityBoardReplyRepository.save(communityBoardReply);

    }

    @Override
    public void remove(Long rno) {

        communityBoardReplyRepository.deleteById(rno);

    }
}
