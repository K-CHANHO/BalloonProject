package org.zerock.balloon.service.qna;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.qna.QnaReplyDTO;
import org.zerock.balloon.entity.qna.QnaBoard;
import org.zerock.balloon.entity.qna.QnaReply;
import org.zerock.balloon.repository.qna.QnaReplyRepository;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor

public class QnaReplyServiceImpl implements QnaReplyService{
    private final QnaReplyRepository replyRepository;

    @Override
    public Long register(QnaReplyDTO qnaReplyDTO) {

        QnaReply qnaReply = dtoToEntity(qnaReplyDTO);

        replyRepository.save(qnaReply);

        return qnaReply.getQbrno();
    }

    @Override
    public List<QnaReplyDTO> getList(Long qbno) {

        List<QnaReply> result =  replyRepository
                .getQnaRepliesByQnaBoardOrderByQbrno(QnaBoard.builder().qbno(qbno).build());

        return result.stream().map(qnaReply -> entityToDTO(qnaReply)).collect(Collectors.toList());
    }

    @Override
    public void modify(QnaReplyDTO qnaReplyDTO) {

        QnaReply qnaReply = dtoToEntity(qnaReplyDTO);

        replyRepository.save(qnaReply);

    }

    @Override
    public void remove(Long qbrno) {

        replyRepository.deleteById(qbrno);
    }
}
