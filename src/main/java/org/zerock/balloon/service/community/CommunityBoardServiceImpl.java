package org.zerock.balloon.service.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.PageResultDTO;
import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.CommunityBoardImage;
import org.zerock.balloon.repository.MemberRepository;
import org.zerock.balloon.repository.community.CommunityBoardGoodRepository;
import org.zerock.balloon.repository.community.CommunityBoardImageRepository;
import org.zerock.balloon.repository.community.CommunityBoardReplyRepository;
import org.zerock.balloon.repository.community.CommunityBoardRepository;

import javax.transaction.Transactional;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class CommunityBoardServiceImpl implements CommunityBoardService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CommunityBoardRepository communityBoardRepository;
    @Autowired
    private CommunityBoardReplyRepository communityBoardReplyRepository;
    @Autowired
    private CommunityBoardImageRepository communityBoardImageRepository;
    @Autowired
    private CommunityBoardGoodRepository communityBoardGoodRepository;

    @Override
    public PageResultDTO<CommunityBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Function<Object[], CommunityBoardDTO> fn=(en->entityToDTO((CommunityBoard)en[0],(Member)en[1],(Long)en[2]));

        Page<Object[]> result = communityBoardRepository.searchPage(
                pageRequestDTO.getSubject(),
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())  );

        return new PageResultDTO<>(result,fn);
    }

    @Override
    public List<CommunityBoardDTO> getTopList() {
        List<Long> bnos = communityBoardGoodRepository.getBoardByGoodCount();

        List<CommunityBoardDTO> boardDTOS = new ArrayList<>();
        bnos.forEach(bno ->{
            int i = 0;
            List<Object[]> result = communityBoardRepository.getBoardByBno(bno);
            CommunityBoard communityBoard = (CommunityBoard) result.get(i)[0];
            Member member = (Member) result.get(i)[1];
            Long replyCount = (Long) result.get(i)[2];
            i++;
            boardDTOS.add(entityToDTO(communityBoard, member, replyCount));
        });
        return boardDTOS;
    }

    @Override
    public List<CommunityBoardDTO> getTopFive() {
        List<Long[]> result = communityBoardRepository.getBoardByBnoTop();
        List<CommunityBoardDTO> boardDTOS = new ArrayList<>();
        for(int i=0; i<result.size(); i++){
            CommunityBoard communityboard = (CommunityBoard) communityBoardRepository.getBoardByBno(result.get(i)[0]).get(0)[0];
            Member member = communityboard.getMember();
            Long replyCount = (Long)communityBoardRepository.getBoardByBno(result.get(i)[0]).get(0)[2];
            System.out.println(entityToDTO(communityboard, member, replyCount)+"*&^%*&^%");
            boardDTOS.add(entityToDTO(communityboard, member, replyCount));
        }


        return  boardDTOS;
    }

    @Override
    public Long register(CommunityBoardDTO communityBoardDTO) {

        Map<String, Object> entityMap = dtoToEntity(communityBoardDTO);

        CommunityBoard communityBoard = (CommunityBoard) entityMap.get("board");
        communityBoardRepository.save(communityBoard);

        List<CommunityBoardImage> communityBoardImageList = (List<CommunityBoardImage>) entityMap.get("imgList");
        if(communityBoardImageList != null){
            communityBoardImageList.forEach(walkRecommendBoardImage -> {
                communityBoardImageRepository.save(walkRecommendBoardImage);
            });
        }

        return communityBoard.getBno();

    }

    @Override
    public CommunityBoardDTO get(Long bno) {
        List<Object[]> result = communityBoardRepository.getBoardByBnoWithImg(bno);

        CommunityBoard communityBoard = (CommunityBoard) result.get(0)[0];
        Member member = (Member) result.get(0)[1];
        Long replyCount = (Long) result.get(0)[3];

        if(result.get(0)[2] != null){
            List<CommunityBoardImage> movieImageList = new ArrayList<>();

            result.forEach(arr -> {
                CommunityBoardImage movieImage = (CommunityBoardImage) arr[2];
                movieImageList.add(movieImage);
            });

            return entityToDTOWithImg(communityBoard, member, movieImageList, replyCount);
        } else{

            result = communityBoardRepository.getBoardByBno(bno);
            communityBoard = (CommunityBoard) result.get(0)[0];
            member = (Member) result.get(0)[1];
            replyCount = (Long) result.get(0)[2];

            return entityToDTO(communityBoard, member, replyCount);
        }

    }

    @Override
    public void modify(CommunityBoardDTO communityBoardDTO) {

        Map<String, Object> entityMap = dtoToEntity(communityBoardDTO);
        CommunityBoard communityBoard = (CommunityBoard) entityMap.get("board");
        communityBoard.changeTitle(communityBoardDTO.getTitle());
        communityBoard.changeContent(communityBoardDTO.getContent());
        communityBoard.changeSubject(communityBoardDTO.getSubject());
        communityBoardRepository.save(communityBoard);

        List<CommunityBoardImage> communityBoardImageList = (List<CommunityBoardImage>) entityMap.get("imgList");
        if(communityBoardImageList != null){
            communityBoardImageList.forEach(walkRecommendBoardImage -> {
                communityBoardImageRepository.save(walkRecommendBoardImage);
            });
        }

    }

    @Transactional
    @Override
    public void remove(Long bno) {
        communityBoardReplyRepository.deleteByBno(bno);
        communityBoardImageRepository.deleteByBno(bno);
        communityBoardGoodRepository.deleteByBno(bno);
        communityBoardRepository.deleteById(bno);
    }

    @Transactional
    @Override
    public void updateHits(Long bno) {
        communityBoardRepository.updateHits(bno); // 조회수
    }
}
