package org.zerock.balloon.controller.qna;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.dto.qna.QnaBoardDTO;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;
import org.zerock.balloon.service.qna.QnaBoardService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/balloon/")
@Log4j2
@RequiredArgsConstructor
public class QnaBoardController{
    private final QnaBoardService qnaboardService;//자동주입.생성자의존성주입
    @GetMapping("/qna") //목록
    public void list(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("result",qnaboardService.getList(pageRequestDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/qna_register") //등록
    public void register(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model){
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
    }

    @PostMapping("/qna_register") //등록
    public String registerPost(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO,Model model, QnaBoardDTO qnaBoardDTO, RedirectAttributes redirectAttributes){
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
        Long qbno=qnaboardService.register(qnaBoardDTO);
        redirectAttributes.addFlashAttribute("msg",qbno);
        return "redirect:/balloon/qna";
    }

    @GetMapping("/qna_read") //상세보기.수정화면
    public String read(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long qbno, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        /* 조회수 로직 */
        Cookie oldCookie = null;
        Cookie[] cookies = servletRequest.getCookies();

        if(balloonAuthMemberDTO != null) {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("qna_" + balloonAuthMemberDTO.getId() + "_postView")) {
                        oldCookie = cookie;
                    }
                }
            }
            log.info("1번 -----------------------------------------------");
            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + qbno.toString() + "]")) {
                    qnaboardService.updateHits(qbno);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + qbno + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24);                            // 쿠키 시간
                    servletResponse.addCookie(oldCookie);
                }
            } else {
                qnaboardService.updateHits(qbno);
                Cookie newCookie = new Cookie("qna_" + balloonAuthMemberDTO.getId() + "_postView", "[" + qbno.toString() + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24);                                // 쿠키 시간
                servletResponse.addCookie(newCookie);
            }
        } else{
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("qnaPostView")) {
                        oldCookie = cookie;
                    }
                }
            }

            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + qbno.toString() + "]")) {
                    qnaboardService.updateHits(qbno);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + qbno + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24 * 365); // 쿠키 시간 1년
                    servletResponse.addCookie(oldCookie);
                }
            } else {
                qnaboardService.updateHits(qbno);
                Cookie newCookie = new Cookie("qnaPostView", "[" + qbno.toString() + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24 * 365);                                // 쿠키 시간
                servletResponse.addCookie(newCookie);
            }
        }

        QnaBoardDTO qnaboardDTO=qnaboardService.get(qbno);
        log.info(qnaboardDTO);
        System.out.println(qnaboardDTO);
        model.addAttribute("dto",qnaboardDTO);
        model.addAttribute("memberDTO", balloonAuthMemberDTO);

        return "balloon/qna_read";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/qna_modify") //상세보기.수정화면
    public String modify(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long qbno, Model model){
        QnaBoardDTO qnaboardDTO=qnaboardService.get(qbno);

        if(qnaboardDTO.getId().equals(balloonAuthMemberDTO.getId())){
            model.addAttribute("requestDTO", pageRequestDTO);
            model.addAttribute("dto", qnaboardDTO);

            return "balloon/qna_modify";
        }else{
            return "balloon/error";
        }
        
    }

    //삭제
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/qna_remove")
    public String remove(long qbno,RedirectAttributes redirectAttributes, @AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO){

        QnaBoardDTO qnaBoardDTO = qnaboardService.get(qbno);

        if(qnaBoardDTO.getId().equals(balloonAuthMemberDTO.getId())) {
            qnaboardService.removeWithReplies(qbno);;
            return "redirect:/balloon/qna";
        } else {
            return "balloon/error";
        }

    }
    //수정
    @PostMapping("/qna_modify")
    public String modify(QnaBoardDTO dto,@ModelAttribute("requestDTO") PageRequestDTO requestDTO,RedirectAttributes redirectAttributes){
        qnaboardService.modify(dto);
        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
        redirectAttributes.addAttribute("qbno",dto.getQbno());

        return "redirect:/balloon/qna_read";
    }
}
