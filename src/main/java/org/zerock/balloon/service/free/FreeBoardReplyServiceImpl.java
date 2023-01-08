package org.zerock.balloon.service.free;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.free.FreeBoardReplyDTO;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.entity.free.FreeBoardReply;
import org.zerock.balloon.repository.free.FreeBoardReplyRepository;
import org.zerock.balloon.service.free.FreeBoardReplyService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreeBoardReplyServiceImpl implements FreeBoardReplyService {

    @Autowired
    FreeBoardReplyRepository freeBoardReplyRepository;

    @Override
    public void register(FreeBoardReplyDTO freeBoardReplyDTO) {
        FreeBoardReply freeBoardReply = dtoToEntity(freeBoardReplyDTO);

      freeBoardReplyRepository.save(freeBoardReply);

    }

    @Override
    public List<FreeBoardReplyDTO> getList(Long bno) {
        List<FreeBoardReply> result = freeBoardReplyRepository.getRepliesByFreeBoardOrderByRno(FreeBoard.builder().bno(bno).build());

        return result.stream().map(freeBoardReply -> entityToDTO(freeBoardReply)).collect(Collectors.toList());

    }

    @Override
    public void modify(FreeBoardReplyDTO freeBoardReplyDTO) {

        FreeBoardReply freeBoardReply = dtoToEntity(freeBoardReplyDTO);

        freeBoardReplyRepository.save(freeBoardReply);

    }

    @Override
    public void remove(Long rno) {

        freeBoardReplyRepository.deleteById(rno);

    }
}
