package org.zerock.balloon.service.free;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.free.FreeBoardDTO;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.PageResultDTO;
import org.zerock.balloon.entity.free.FreeBoard;
import org.zerock.balloon.entity.free.FreeBoardImage;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.repository.MemberRepository;
import org.zerock.balloon.repository.free.FreeBoardGoodRepository;
import org.zerock.balloon.repository.free.FreeBoardImageRepository;
import org.zerock.balloon.repository.free.FreeBoardRepository;
import org.zerock.balloon.repository.free.FreeBoardReplyRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
@Service
@RequiredArgsConstructor
@Log4j2
public class FreeBoardServiceImpl implements FreeBoardService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FreeBoardRepository freeBoardRepository;
    @Autowired
    private FreeBoardReplyRepository freeBoardReplyRepository;
    @Autowired
    private FreeBoardImageRepository freeBoardImageRepository;
    @Autowired
    private FreeBoardGoodRepository freeBoardGoodRepository;

    @Override
    public PageResultDTO<FreeBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Function<Object[], FreeBoardDTO> fn=(en->entityToDTO((FreeBoard)en[0],(Member)en[1],(Long)en[2]));

        Page<Object[]> result = freeBoardRepository.searchPage(
                pageRequestDTO.getSubject(),
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())  );

        return new PageResultDTO<>(result,fn);
    }


    @Override
    public List<FreeBoardDTO> getTopList() {
        List<Long> bnos = freeBoardGoodRepository.getBoardByGoodCount();

        List<FreeBoardDTO> boardDTOS = new ArrayList<>();
        bnos.forEach(bno ->{
            int i = 0;
            List<Object[]> result = freeBoardRepository.getBoardByBno(bno);
            FreeBoard freeBoard = (FreeBoard) result.get(i)[0];
            Member member = (Member) result.get(i)[1];
            Long replyCount = (Long) result.get(i)[2];
            i++;
            boardDTOS.add(entityToDTO(freeBoard, member, replyCount));
        });
        return boardDTOS;
    }

    @Override
    public List<FreeBoardDTO> getTopFive() {
        List<Long[]> result = freeBoardRepository.getBoardByBnoTop();
        List<FreeBoardDTO> boardDTOS = new ArrayList<>();
        for(int i=0; i<result.size(); i++){
            FreeBoard freeboard = (FreeBoard) freeBoardRepository.getBoardByBno(result.get(i)[0]).get(0)[0];
            Member member = freeboard.getMember();
            Long replyCount = (Long)freeBoardRepository.getBoardByBno(result.get(i)[0]).get(0)[2];
            System.out.println(entityToDTO(freeboard, member, replyCount)+"*&^%*&^%");
            boardDTOS.add(entityToDTO(freeboard, member, replyCount));
        }


        return  boardDTOS;
    }


    @Override
    public Long register(FreeBoardDTO freeBoardDTO) {

        Map<String, Object> entityMap = dtoToEntity(freeBoardDTO);

        FreeBoard freeBoard = (FreeBoard) entityMap.get("board");
        freeBoardRepository.save(freeBoard);

        List<FreeBoardImage> freeBoardImageList = (List<FreeBoardImage>) entityMap.get("imgList");
        if(freeBoardImageList != null){
            freeBoardImageList.forEach(freeBoardImage -> {
                freeBoardImageRepository.save(freeBoardImage);
            });
        }

        return freeBoard.getBno();

    }


    @Override
    public FreeBoardDTO get(Long bno) {
        List<Object[]> result = freeBoardRepository.getBoardByBnoWithImg(bno);

        FreeBoard freeBoard = (FreeBoard) result.get(0)[0];
        Member member = (Member) result.get(0)[1];
        Long replyCount = (Long) result.get(0)[3];

        if(result.get(0)[2] != null){
            List<FreeBoardImage> movieImageList = new ArrayList<>();

            result.forEach(arr -> {
                FreeBoardImage movieImage = (FreeBoardImage) arr[2];
                movieImageList.add(movieImage);
            });

            return entityToDTOWithImg(freeBoard, member, movieImageList, replyCount);
        } else{

            result = freeBoardRepository.getBoardByBno(bno);
            freeBoard = (FreeBoard) result.get(0)[0];
            member = (Member) result.get(0)[1];
            replyCount = (Long) result.get(0)[2];

            return entityToDTO(freeBoard, member, replyCount);
        }

    }


    @Override
    public void modify(FreeBoardDTO freeBoardDTO) {

        Map<String, Object> entityMap = dtoToEntity(freeBoardDTO);
        FreeBoard freeBoard = (FreeBoard) entityMap.get("board");
        freeBoard.changeTitle(freeBoardDTO.getTitle());
        freeBoard.changeContent(freeBoardDTO.getContent());
        freeBoardRepository.save(freeBoard);

        List<FreeBoardImage> freeBoardImageList = (List<FreeBoardImage>) entityMap.get("imgList");
        if(freeBoardImageList != null){
            freeBoardImageList.forEach(FreeBoardImage -> {
                freeBoardImageRepository.save(FreeBoardImage);
            });
        }

    }

    @Transactional
    @Override
    public void remove(Long bno) {
        freeBoardReplyRepository.deleteByBno(bno);
        freeBoardImageRepository.deleteByBno(bno);
        freeBoardGoodRepository.deleteByBno(bno);
        freeBoardRepository.deleteById(bno);
    }

    @Transactional
    @Override
    public void updateHits(Long qbno) {
        freeBoardRepository.updateHits(qbno); // 조회수
    }
}
