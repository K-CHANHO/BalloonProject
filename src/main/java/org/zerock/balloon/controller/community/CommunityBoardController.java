package org.zerock.balloon.controller.community;

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
import org.zerock.balloon.dto.community.CommunityBoardDTO;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;
import org.zerock.balloon.service.community.CommunityBoardService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/balloon/")
public class CommunityBoardController {

    @Autowired
    private CommunityBoardService communityBoardService;

    @GetMapping("/community") // 게시판 리스트
    public void list(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
        model.addAttribute("toplist", communityBoardService.getTopList());
        model.addAttribute("result", communityBoardService.getList(pageRequestDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/community_register") // 게시물 등록
    public void register(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model){
        //로그인 한 유저 정보 넘기기
        model.addAttribute("memberDTO", balloonAuthMemberDTO);

    }

    @PostMapping("/community_register")
    public String registerPost(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model, CommunityBoardDTO communityBoardDTO, RedirectAttributes redirectAttributes){
        //로그인 한 유저 정보 넘기기
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
        Long bno = communityBoardService.register(communityBoardDTO);

        return "redirect:/balloon/community_read?bno="+bno;
    }

    @GetMapping("/community_read")
    public String read(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Long bno, PageRequestDTO pageRequestDTO, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        /* 조회수 로직 */
        Cookie oldCookie = null;
        Cookie[] cookies = servletRequest.getCookies();
        if(balloonAuthMemberDTO != null) {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("community_" + balloonAuthMemberDTO.getId() + "_PostView")) {
                        oldCookie = cookie;
                    }
                }
            }

            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + bno.toString() + "]")) {
                    communityBoardService.updateHits(bno);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + bno + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24 * 365); // 쿠키 시간 1년
                    servletResponse.addCookie(oldCookie);
                }
            } else {
                communityBoardService.updateHits(bno);
                Cookie newCookie = new Cookie("community_" + balloonAuthMemberDTO.getId() + "_PostView", "[" + bno.toString() + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24 * 365);                                // 쿠키 시간
                servletResponse.addCookie(newCookie);
            }
        } else{
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("communityPostView")) {
                        oldCookie = cookie;
                    }
                }
            }

            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + bno.toString() + "]")) {
                    communityBoardService.updateHits(bno);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + bno + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24 * 365); // 쿠키 시간 1년
                    servletResponse.addCookie(oldCookie);
                }
            } else {
                communityBoardService.updateHits(bno);
                Cookie newCookie = new Cookie("communityPostView", "[" + bno.toString() + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24 * 365); // 쿠키 시간
                servletResponse.addCookie(newCookie);
            }
        }

            CommunityBoardDTO boardDTO = communityBoardService.get(bno);

            model.addAttribute("dto", boardDTO);
            model.addAttribute("requestDTO", pageRequestDTO);
            model.addAttribute("memberDTO", balloonAuthMemberDTO);

            return "balloon/community_read";

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/community_modify")
    public String modify(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, PageRequestDTO pageRequestDTO, Long bno, Model model) {

        CommunityBoardDTO communityBoardDTO = communityBoardService.get(bno);

        if(communityBoardDTO.getId().equals(balloonAuthMemberDTO.getId())) {
            model.addAttribute("requestDTO", pageRequestDTO);
            model.addAttribute("dto", communityBoardDTO);

            return "balloon/community_modify";
        }
        else{
            return "balloon/error";
        }

    }

    @PostMapping("/community_modify")
    public String modify(CommunityBoardDTO communityBoardDTO, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
        communityBoardService.modify(communityBoardDTO);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", communityBoardDTO.getBno());

        return "redirect:/balloon/community_read";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/community_remove")
    public String remove(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Long bno, RedirectAttributes redirectAttributes) {

        CommunityBoardDTO communityBoardDTO = communityBoardService.get(bno);

        if(communityBoardDTO.getId().equals(balloonAuthMemberDTO.getId())) {
            communityBoardService.remove(bno);
            return "redirect:/balloon/community";
        } else {
            return "balloon/error";
        }
    }



}
