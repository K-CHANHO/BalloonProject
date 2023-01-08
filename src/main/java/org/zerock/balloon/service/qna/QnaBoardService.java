package org.zerock.balloon.service.qna;

import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.dto.qna.QnaBoardDTO;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.PageResultDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.qna.QnaBoard;

import java.util.List;

public interface QnaBoardService {
    QnaBoardDTO get(Long qbno); //상세보기
    Long register(QnaBoardDTO qnaBoardDTO); //등록

    List<QnaBoardDTO> getTopFive();

    void removeWithReplies(Long qbno);//삭제

    void updateHits(Long qbno);

    void modify(QnaBoardDTO boardDTO);//수정
    PageResultDTO<QnaBoardDTO,Object[]> getList(PageRequestDTO pageRequestDTO); //목록
    default QnaBoard dtoToEntity(QnaBoardDTO qnadto){
        Member member= Member.builder().id(qnadto.getId()).build();
        QnaBoard board=QnaBoard.builder()
                .qbno(qnadto.getQbno())
                .title(qnadto.getQbtitle())
                .content(qnadto.getQbcontent())
                .member(member)
                .hits(qnadto.getQbhits())
                .build();
        return board;
    }
    default QnaBoardDTO entityToDTO(QnaBoard qnaboard,Member member,Long qbreplyCount){
        QnaBoardDTO boardDTO=QnaBoardDTO.builder()
                .qbno(qnaboard.getQbno())
                .qbtitle(qnaboard.getTitle())
                .qbcontent(qnaboard.getContent())
                .regDate(qnaboard.getRegDate())
                .modDate(qnaboard.getModDate())
                .id(member.getId())
                .nickname(member.getNickname())
                .qbreplyCount(qbreplyCount.intValue())
                .qbhits(qnaboard.getHits())
                .build();
        return boardDTO;
    }
}
