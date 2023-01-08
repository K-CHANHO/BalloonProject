package org.zerock.balloon.service.qna;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.dto.qna.QnaBoardDTO;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.PageResultDTO;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.qna.QnaBoard;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.repository.qna.QnaBoardRepository;
import org.zerock.balloon.repository.qna.QnaReplyRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
@Service
@RequiredArgsConstructor
@Log4j2
public class QnaBoardServiceImpl implements QnaBoardService {
    private final QnaBoardRepository qnarepository; //자동주입. 생성자의존성주입.
    private final QnaReplyRepository qnaReplyRepository; //자동주입. 생성자의존성주입

    @Override
    public QnaBoardDTO get(Long qbno) {
        Object result=qnarepository.getBoardByBno(qbno).get(0);
        Object[] arr=(Object[])result;
        return entityToDTO((QnaBoard)arr[0],(Member)arr[1],(Long)arr[2]);
    }

    @Override
    public List<QnaBoardDTO> getTopFive() {
        List<Long[]> result = qnarepository.getBoardByBnoTop();
        List<QnaBoardDTO> boardDTOS = new ArrayList<>();
        for(int i=0; i<result.size(); i++){
            QnaBoard qnaBoard = (QnaBoard) qnarepository.getBoardByBno(result.get(i)[0]).get(0)[0];
            Member member = qnaBoard.getMember();
            Long replyCount = (Long)qnarepository.getBoardByBno(result.get(i)[0]).get(0)[2];
            boardDTOS.add(entityToDTO(qnaBoard, member, replyCount));
        }

        return  boardDTOS;
    }

    @Override
    public Long register(QnaBoardDTO qnaBoardDTO) {
        QnaBoard board=dtoToEntity(qnaBoardDTO);
        qnarepository.save(board);
        return board.getQbno();
    }
    @Transactional
    @Override
    public void removeWithReplies(Long qbno) {
        qnaReplyRepository.deleteByQbno(qbno);//댓글삭제
        //이미지 삭제
        qnarepository.deleteById(qbno);//부모글삭제
    }

    @Override
    public void modify(QnaBoardDTO qnaBoardDTO) {
        //        Board board=repository.getOne(boardDTO.getBno()); //deprecated.
        QnaBoard qnaBoard=qnarepository.findById(qnaBoardDTO.getQbno()).get();
        qnaBoard.changeTitle(qnaBoardDTO.getQbtitle());
        qnaBoard.changeContent(qnaBoardDTO.getQbcontent());
        qnarepository.save(qnaBoard);
    }

    @Override
    public PageResultDTO<QnaBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Function<Object[],QnaBoardDTO> qn=(en->entityToDTO((QnaBoard)en[0],(Member)en[1],(Long)en[2]));
//        Page<Object[]> result=qnarepository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("qbno").descending()));
        Page<Object[]> result = qnarepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("qbno").descending())  ); //descending => desc, dto 역순으로

        return new PageResultDTO<>(result,qn);
    }

    @Transactional
    @Override
    public void updateHits(Long qbno) {
        qnarepository.updateHits(qbno); // 조회수
    }

}
