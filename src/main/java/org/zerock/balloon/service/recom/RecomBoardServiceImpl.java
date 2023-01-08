package org.zerock.balloon.service.recom;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.PageResultDTO;
import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.dto.recom.RecomBoardDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.recom.RecomBoard;
import org.zerock.balloon.entity.recom.RecomBoardImage;
import org.zerock.balloon.repository.recom.RecomBoardGoodRepository;
import org.zerock.balloon.repository.recom.RecomBoardImageRepository;
import org.zerock.balloon.repository.recom.RecomBoardReplyRepository;
import org.zerock.balloon.repository.recom.RecomBoardRepository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class RecomBoardServiceImpl implements RecomBoardService{
    private final RecomBoardRepository recomBoardRepository; //자동주입. 생성자의존성주입.
    private final RecomBoardReplyRepository recomBoardReplyRepository; //자동주입. 생성자의존성주입
    private final RecomBoardImageRepository recomBoardImageRepository; //자동주입. 생성자의존성주입

    private final RecomBoardGoodRepository recomBoardGoodRepository;

    @Override
    public RecomBoardDTO get(Long bno) {
        List<Object[]> result = recomBoardRepository.getBoardByBnoWithImg(bno);

        RecomBoard recomBoard = (RecomBoard)result.get(0)[0];

        Member member = (Member)result.get(0)[2];

        Long replyCount = (Long)result.get(0)[3];
        log.info(result);
        log.info(member);

        if(result.get(0)[1] != null){

            List<RecomBoardImage> recomBoardImageList = new ArrayList<>();

            result.forEach(arr -> {
                RecomBoardImage recomBoardImage = (RecomBoardImage)arr[1];
                recomBoardImageList.add(recomBoardImage);
            });

            return entityToDTOWithImg(recomBoard,recomBoardImageList,member,replyCount);
        }
        else{
            result = recomBoardRepository.getBoardByBno(bno);

            recomBoard = (RecomBoard)result.get(0)[0];

            member = (Member)result.get(0)[1];

            replyCount = (Long)result.get(0)[2];

            return entityToDTO(recomBoard,member,replyCount);
        }
    }
    @Override
    public List<RecomBoardDTO> getTopList() {
        List<Long[]> bnos = recomBoardGoodRepository.getBoardByGoodCount();

        List<RecomBoardDTO> boardDTOS = new ArrayList<>();
        bnos.forEach(bno ->{
            int i = 0;
            List<Object[]> result = recomBoardRepository.getBoardByBno(bno[0]);
            RecomBoard recomBoard = (RecomBoard) result.get(i)[0];
            Member member = (Member) result.get(i)[1];
            Long replyCount = (Long) result.get(i)[2];
            i++;
            boardDTOS.add(entityToDTO(recomBoard, member, replyCount));
        });
        return boardDTOS;
    }

    @Override
    public List<RecomBoardDTO> getTopFive() {
        List<Long[]> result = recomBoardRepository.getBoardByBnoTop();
        System.out.println(result + "@@@@@@@@@@");
        List<RecomBoardDTO> boardDTOS = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            RecomBoard recomboard = (RecomBoard) recomBoardRepository.getBoardByBno(result.get(i)[0]).get(0)[0];
            Member member = recomboard.getMember();
            Long replyCount = (Long) recomBoardRepository.getBoardByBno(result.get(i)[0]).get(0)[2];
            System.out.println(entityToDTO(recomboard, member, replyCount) + "*&^%*&^%");
            boardDTOS.add(entityToDTO(recomboard, member, replyCount));
        }


        return boardDTOS;
    }

    @Transactional
    @Override
    public Long register(RecomBoardDTO recomBoardDTO) {
        Map<String, Object> entityMap =dtoToEntity(recomBoardDTO);
        RecomBoard board= (RecomBoard) entityMap.get("board");
        List<RecomBoardImage> recomBoardImageList = (List<RecomBoardImage>) entityMap.get("imgList");

        recomBoardRepository.save(board);

        if(recomBoardImageList !=null){
            recomBoardImageList.forEach(recomBoardImage -> {
                recomBoardImageRepository.save(recomBoardImage);
            });
        }


        return board.getBno();
    }
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        recomBoardReplyRepository.deleteByBno(bno);//댓글삭제
        recomBoardImageRepository.deleteByBno(bno);//이미지 삭제
        recomBoardGoodRepository.deleteByBno(bno);//좋아요 삭제
        recomBoardRepository.deleteById(bno);//부모글삭제
    }

    @Override
    public void modify(RecomBoardDTO recomBoardDTO) {
        Map<String, Object> entityMap = dtoToEntity(recomBoardDTO);
        RecomBoard recomBoard = (RecomBoard) entityMap.get("board");
        recomBoard.changeTitle(recomBoardDTO.getTitle());
        recomBoard.changeContent(recomBoardDTO.getContent());
        recomBoardRepository.save(recomBoard);

        List<RecomBoardImage> recomBoardImageList = (List<RecomBoardImage>) entityMap.get("imgList");
        if(recomBoardImageList != null){
            recomBoardImageList.forEach(recomBoardImage -> {
                recomBoardImageRepository.save(recomBoardImage);
            });
        }
    }

    @Override
    public PageResultDTO<RecomBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Function<Object[],RecomBoardDTO> fn=(en->entityToDTO((RecomBoard)en[0],(Member)en[1],(Long)en[2]));
//        Page<Object[]> result=recomrepository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = recomBoardRepository.searchPage(
                pageRequestDTO.getSubject(),
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())  ); //descending => desc, dto 역순으로

        return new PageResultDTO<>(result,fn);
    }

    @Transactional
    @Override
    public void updateHits(Long bno) {
        recomBoardRepository.updateHits(bno); // 조회수
    }
}
