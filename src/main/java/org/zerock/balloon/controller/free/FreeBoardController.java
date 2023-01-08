package org.zerock.balloon.controller.free;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.free.FreeBoardDTO;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;
import org.zerock.balloon.service.free.FreeBoardService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/balloon/")
public class FreeBoardController {

    @Autowired
    private FreeBoardService freeBoardService;

    @GetMapping("/free") // 게시판 리스트
    public void list(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, PageRequestDTO pageRequestDTO, Model model){

        model.addAttribute("toplist", freeBoardService.getTopList());
        model.addAttribute("result", freeBoardService.getList(pageRequestDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/free_register") // 게시물 등록
    public void register(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model){
        //로그인 한 유저 정보 넘기기
        model.addAttribute("memberDTO", balloonAuthMemberDTO);

    }

    @PostMapping("/free_register")
    public String registerPost(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model, FreeBoardDTO freeBoardDTO, RedirectAttributes redirectAttributes){
        //로그인 한 유저 정보 넘기기
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
        Long bno = freeBoardService.register(freeBoardDTO);

        return "redirect:/balloon/free_read?bno="+bno;
    }

    @GetMapping("/free_read")
    public String read(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Long bno, PageRequestDTO pageRequestDTO, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse){
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

            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + bno.toString() + "]")) {
                    freeBoardService.updateHits(bno);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + bno + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24);                            // 쿠키 시간
                    servletResponse.addCookie(oldCookie);
                }
            } else {
                freeBoardService.updateHits(bno);
                Cookie newCookie = new Cookie("qna_" + balloonAuthMemberDTO.getId() + "_postView", "[" + bno.toString() + "]");
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
                if (!oldCookie.getValue().contains("[" + bno.toString() + "]")) {
                    freeBoardService.updateHits(bno);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + bno + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24 * 365); // 쿠키 시간 1년
                    servletResponse.addCookie(oldCookie);
                }
            } else {
                freeBoardService.updateHits(bno);
                Cookie newCookie = new Cookie("freePostView", "[" + bno.toString() + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24 * 365);                                // 쿠키 시간
                servletResponse.addCookie(newCookie);
            }
        }


        FreeBoardDTO boardDTO = freeBoardService.get(bno);

        model.addAttribute("dto", boardDTO);
        model.addAttribute("requestDTO", pageRequestDTO);
        model.addAttribute("memberDTO", balloonAuthMemberDTO);

        return "balloon/free_read";

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/free_modify")
    public String modify(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, PageRequestDTO pageRequestDTO, Long bno, Model model) {

        FreeBoardDTO freeBoardDTO = freeBoardService.get(bno);

        System.out.println(freeBoardDTO);
        System.out.println(balloonAuthMemberDTO);

        if(freeBoardDTO.getId().equals(balloonAuthMemberDTO.getId())) {
            model.addAttribute("requestDTO", pageRequestDTO);
            model.addAttribute("dto", freeBoardDTO);

            return "balloon/free_modify";
        }
        else{
            return "balloon/error";
        }

    }

    @PostMapping("/free_modify")
    public String modify(FreeBoardDTO freeBoardDTO, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        freeBoardService.modify(freeBoardDTO);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", freeBoardDTO.getBno());

        return "redirect:/balloon/free_read";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/free_remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {
        freeBoardService.remove(bno);

        return "redirect:/balloon/free";
    }



}
