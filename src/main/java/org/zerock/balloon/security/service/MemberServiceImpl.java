package org.zerock.balloon.security.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.repository.MemberRepository;
import org.zerock.balloon.repository.community.CommunityBoardGoodRepository;
import org.zerock.balloon.repository.community.CommunityBoardImageRepository;
import org.zerock.balloon.repository.community.CommunityBoardReplyRepository;
import org.zerock.balloon.repository.community.CommunityBoardRepository;
import org.zerock.balloon.repository.free.FreeBoardGoodRepository;
import org.zerock.balloon.repository.free.FreeBoardImageRepository;
import org.zerock.balloon.repository.free.FreeBoardReplyRepository;
import org.zerock.balloon.repository.free.FreeBoardRepository;
import org.zerock.balloon.repository.qna.QnaBoardImageRepository;
import org.zerock.balloon.repository.qna.QnaBoardRepository;
import org.zerock.balloon.repository.qna.QnaReplyRepository;
import org.zerock.balloon.repository.recom.RecomBoardGoodRepository;
import org.zerock.balloon.repository.recom.RecomBoardImageRepository;
import org.zerock.balloon.repository.recom.RecomBoardReplyRepository;
import org.zerock.balloon.repository.recom.RecomBoardRepository;
import org.zerock.balloon.security.dto.MemberDTO;
import org.zerock.balloon.security.dto.ResultDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final BalloonUserDetailsService balloonUserDetailsService;

    // 산책로 커뮤니티
    private final CommunityBoardRepository communityBoardRepository;
    private final CommunityBoardImageRepository communityBoardImageRepository;
    private final CommunityBoardReplyRepository communityBoardReplyRepository;
    private final CommunityBoardGoodRepository communityBoardGoodRepository;

    //자유 게시판
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardImageRepository freeBoardImageRepository;
    private final FreeBoardReplyRepository freeBoardReplyRepository;
    private final FreeBoardGoodRepository freeBoardGoodRepository;

    //산책로 추천 게시판
    private final RecomBoardRepository recomBoardRepository;
    private final RecomBoardImageRepository recomBoardImageRepository;
    private final RecomBoardReplyRepository recomBoardReplyRepository;
    private final RecomBoardGoodRepository recomBoardGoodRepository;

    //QnA 게시판
    private final QnaBoardRepository qnaBoardRepository;
    private final QnaBoardImageRepository qnaBoardImageRepository;
    private final QnaReplyRepository qnaReplyRepository;


    //회원가입
    @Override
    public ResultDTO joinMember(MemberDTO memberDTO) {
        ResultDTO resultDTO = new ResultDTO();
        memberDTO.setPw(passwordEncoder.encode(memberDTO.getPw()));
        Member member = MemberService.memberDTOToEntity(memberDTO);
        Member memberInfo = memberRepository.save(member);
        log.info("회원가입 성공한 멤버" + memberInfo);
        if(memberInfo.getId() != null) {
            resultDTO.setResult(true);
            resultDTO.setMessage("회원가입되었습니다.");
        }else{
            resultDTO.setResult(false);
            resultDTO.setMessage("회원가입에 실패하였습니다.");
        }
        return resultDTO;
    }

    /**
     * 아이디 중복 체크
     */
    @Override
    public ResultDTO getExistUserId(String id) {
        ResultDTO resultDTO = new ResultDTO();
        boolean isExist = memberRepository.existsById(id);
        if(isExist) {
            resultDTO.setResult(false);
            resultDTO.setMessage("사용중인 아이디 입니다. 다시 입력해주세요.");
        } else {
            resultDTO.setResult(true);
            resultDTO.setMessage("사용 가능한 아이디입니다.");
        }
        return resultDTO;
    }

    /**
     * 이메일 중복 체크
     */
    @Override
    public ResultDTO getExistUserEmail(String userEmail) {
        ResultDTO resultDTO = new ResultDTO();
        List<Member> members = memberRepository.findAllByEmail(userEmail);
        log.info(">>>>>> members: {}", members);
        if(!members.isEmpty()) {
            resultDTO.setResult(false);
            resultDTO.setMessage("사용중인 이메일입니다. 다시 입력해주세요.");
        } else{
            resultDTO.setResult(true);
            resultDTO.setMessage("사용 가능한 이메일입니다.");
        }
        return resultDTO;
    }

    /**
     * 닉네임 중복 체크
     */

    @Override
    public ResultDTO getExistUserNickname(String userNickname) {
        ResultDTO resultDTO = new ResultDTO();
        List<Member> members = memberRepository.findByNickname(userNickname);
        if(!members.isEmpty()) {
            resultDTO.setResult(false);
            resultDTO.setMessage("사용중인 닉네임입니다. 다시 입력해주세요.");
        } else{
            resultDTO.setResult(true);
            resultDTO.setMessage("사용 가능한 닉네임입니다.");
        }
        return resultDTO;
    }

    /**
     * 회원 정보 변경
     */
    @Override
    public ResultDTO changePw(MemberDTO memberDTO) {
        memberDTO.setPw(passwordEncoder.encode(memberDTO.getPw()));
        Member member = MemberService.memberDTOToEntity(memberDTO);
        log.info("비밀번호 변경하러 간다 : "+member.getId() + member.getPw());
        Member members = memberRepository.save(member);
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setResult(true);
        resultDTO.setMessage("비밀번호가 변경되었습니다.");
        resultDTO.setChangedInfo(member.getPw());
        return resultDTO;
    }

    @Override
    public ResultDTO changeNickname(MemberDTO memberDTO) {
        Member member = MemberService.memberDTOToEntity(memberDTO);
        memberRepository.save(member);
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setResult(true);
        resultDTO.setMessage("닉네임이 변경되었습니다.");
        resultDTO.setChangedInfo(member.getNickname());
        return resultDTO;
    }

    @Override
    public ResultDTO changeName(MemberDTO memberDTO) {
        Member member = MemberService.memberDTOToEntity(memberDTO);
        memberRepository.save(member);
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setResult(true);
        resultDTO.setMessage("이름이 변경되었습니다.");
        resultDTO.setChangedInfo(member.getName());
        return resultDTO;
    }

    /**
     * 회원 탈퇴. 관련 정보, 데이터 삭제
     * @param id
     */

    @Override
    public void deleteMember(String id) {

        log.info("삭제할 ID:" + id);

        //추천 게시판 정보 삭제
        List<Long> recomBons = recomBoardRepository.getBnoById(id);
        if(recomBons.size()>0){
            recomBons.forEach(bno -> {
                log.info("들어가니???");
                recomBoardImageRepository.deleteByBno(bno);
                recomBoardReplyRepository.deleteByBno(bno);
                recomBoardGoodRepository.deleteByBno(bno);
                recomBoardRepository.deleteById(bno);
            });
        }
        recomBoardGoodRepository.deleteById(id);

        //산책로 커뮤니티 정보 삭제
        List<Long> comBnos = communityBoardRepository.getBnoById(id); //아이디로 게시판 번호 불러오기
        if(comBnos.size()>0) {
            comBnos.forEach(bno -> {
                communityBoardImageRepository.deleteByBno(bno);
                communityBoardReplyRepository.deleteByBno(bno); //id로 삭제 가능
                communityBoardGoodRepository.deleteByBno(bno); // id로 삭제 가능
                communityBoardRepository.deleteById(bno); //id로 삭제 가능
            });
        }
        communityBoardGoodRepository.deleteById(id);


        // 자유게시판 정보 삭제

        List<Long> freeBnos = freeBoardRepository.getBnoById(id);
        if(freeBnos.size()>0) {
            freeBnos.forEach(bno -> {
                freeBoardImageRepository.deleteByBno(bno);
                freeBoardReplyRepository.deleteByBno(bno);
                freeBoardGoodRepository.deleteByBno(bno);
                freeBoardRepository.deleteById(bno);
            });
        }

        //QnA게시판 정보 삭제
        List<Long> qnaBnos = qnaBoardRepository.getBnoById(id);
        if(qnaBnos.size()>0) {
            qnaBnos.forEach(bno -> {
                //좋아요랑 댓글 없음
                qnaReplyRepository.deleteByQbno(bno);
                qnaBoardRepository.deleteById(bno);
            });
        }

        memberRepository.deleteById(id);

    }


}
