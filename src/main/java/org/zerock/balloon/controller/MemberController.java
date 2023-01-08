package org.zerock.balloon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.balloon.repository.MemberRepository;
import org.zerock.balloon.repository.community.CommunityBoardReplyRepository;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;
import org.zerock.balloon.security.dto.MemberDTO;
import org.zerock.balloon.security.dto.ResultDTO;
import org.zerock.balloon.security.service.BalloonOAuth2UserDetailsService;
import org.zerock.balloon.security.service.BalloonUserDetailsService;
import org.zerock.balloon.security.service.MemberService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/balloon/")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final BalloonUserDetailsService userDetailsService;
    private final BalloonOAuth2UserDetailsService oAuth2UserDetailsService;
    private final MemberService memberService;
    private final CommunityBoardReplyRepository communityBoardReplyRepository;
    private final MemberRepository memberRepository;

    //시작 페이지
    @GetMapping({"/index",""})
    public String start(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model){
        log.info("시작 페이지로 넘어갈 회원 정보..........." + balloonAuthMemberDTO);
        model.addAttribute("memberDTO", balloonAuthMemberDTO);

        return "/balloon/index";
    }

    /**
     * 로그인
     * /auth/login : 로그인 실패시 띄울 메시지 처리
     * /login : 로그인 페이지
     * @param exception
     * @param model
     * @return
     */
    @GetMapping("/auth/login")
    public String loginCheck( @RequestParam(value = "exception", required = false) String exception,
                              Model model){
        model.addAttribute("exception", exception);
        log.info("로그인 실패 메시지 출력:" + exception);
        return "/balloon/login";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO,
                        Model model){
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getName();
        log.info("이건 뭐가 나오는거지...?" + auth.getName());

        //로그인 후에 로그인 화면 넘어가는 거 방지
        //로그인 안 한 유저이면
        if(auth.getName().equals("anonymousUser")){
            return null;
        }else{ //아니면
            //return "redirect:"+ referer;
            return "/balloon/main";
        }
    }


//    //메인페이지
//    @GetMapping("/main")
//    public void main(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model){
//        model.addAttribute("memberDTO", balloonAuthMemberDTO);
//        log.info("메인에 넘어가는 로그인 유저 정보: " + balloonAuthMemberDTO);
//    }

    /**
     * 회원가입 처리
     * @return
     */
    @GetMapping("/join_member")
    public String join(){
        return "balloon/join_member";
    }


    //중복 체크
    @GetMapping("/exist/id/{userId}")
    public @ResponseBody ResultDTO getExistUserId(@PathVariable String userId) {
        ResultDTO resultDTO = memberService.getExistUserId(userId);
        return resultDTO;
    }

    @GetMapping("/exist/email/{userEmail}")
    public @ResponseBody ResultDTO getExistUserEmail(@PathVariable String userEmail){
        ResultDTO resultDTO = memberService.getExistUserEmail(userEmail);
        return resultDTO;
    }

    @GetMapping("/exist/nickname/{userNickname}")
    public  @ResponseBody ResultDTO getExistUserNickname(@PathVariable String userNickname){
        ResultDTO resultDTO = memberService.getExistUserNickname(userNickname);
        return resultDTO;
    }

    @PostMapping("/join_member")
    public @ResponseBody ResultDTO joinMember(@RequestBody MemberDTO memberDTO){
        log.info(">>>>>> memberDTO: {}", memberDTO);
        ResultDTO resultDTO = memberService.joinMember(memberDTO);
        return resultDTO;
    }

    /**
     * 회원 정보 수정
     */
    @GetMapping("/my_info")
    public String memberInfo(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model){
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getName();
        log.info("이건 뭐가 나오는거지...?" + auth.getName());
        log.info("정보 수정으로 넘어갈 멤버 정보: " +balloonAuthMemberDTO);

        //로그인 안 한 유저이면 - 멤버만 마이페이지 보이게 처리해서 필요 없어진 코드
//        if(auth.getName().equals("anonymousUser")) {
//            return "/balloon/main";
//        } else{
//            return "/balloon/my_info";
//        }
        return "/balloon/my_info";
    }

    @PutMapping("/my_info/pw")
    public @ResponseBody ResultDTO updatePw(@RequestBody MemberDTO memberDTO, @AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO){
        log.info("비밀번호 변경시 넘어갈 정보: "+memberDTO);
        ResultDTO resultDTO = memberService.changePw(memberDTO);
        balloonAuthMemberDTO.setPw(resultDTO.getChangedInfo());
        return resultDTO;
    }

    @PutMapping("/my_info/nickname")
    public @ResponseBody ResultDTO updateNickname(@RequestBody MemberDTO memberDTO, @AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO){
        log.info("닉네임 변경시 넘어갈 정보: "+memberDTO);
        ResultDTO resultDTO = memberService.changeNickname(memberDTO);
        balloonAuthMemberDTO.setNickname(resultDTO.getChangedInfo());

        //닉네임 변경시 각 게시판 댓글 닉네임 변경 처리
        communityBoardReplyRepository.updateNicknameById(balloonAuthMemberDTO.getId(), balloonAuthMemberDTO.getNickname());
        return resultDTO;
    }

//    @PostMapping("/my_info/nickname")
//    public ResponseEntity<String> updateNickname(@RequestBody MemberDTO memberDTO, Model model) {
//
//        ResultDTO resultDTO = memberService.changeNickname(memberDTO);
//        /* 변경된 세션 등록 */
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(memberDTO.getId(), memberDTO.getPw()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        model.addAttribute("resultDTO", resultDTO);
//        return new ResponseEntity<>("success", HttpStatus.OK);
//    }

    @PutMapping("/my_info/name")
    public @ResponseBody ResultDTO updateName(@RequestBody MemberDTO memberDTO, @AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO){
        ResultDTO resultDTO = memberService.changeName(memberDTO);
        balloonAuthMemberDTO.setName(resultDTO.getChangedInfo());
        return resultDTO;
    }

    /**
     * 회원 탈퇴
     * @param userId
     * @return
     */
    @DeleteMapping("/my_info/delete/{userId}")
    public String deleteMember(@PathVariable String userId){
        log.info("컨트롤러에 들어온 삭제할 id: " + userId);
        memberService.deleteMember(userId);
        SecurityContextHolder.clearContext();//시큐리티 탈퇴. 로그아웃 처리
        return "/balloon/index";
    }

}