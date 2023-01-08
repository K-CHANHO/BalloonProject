package org.zerock.balloon.controller.recom;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.dto.recom.RecomBoardDTO;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;
import org.zerock.balloon.service.recom.RecomBoardGoodService;
import org.zerock.balloon.service.recom.RecomBoardService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/balloon/")
@Log4j2
@RequiredArgsConstructor
public class RecomBoardController {
    private final RecomBoardService recomBoardService;//자동주입.생성자의존성주입

    private final RecomBoardGoodService recomBoardGoodService;

    @GetMapping("/recom") //목록
    public void list(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("good", recomBoardGoodService.getGood());
        model.addAttribute("toplist", recomBoardService.getTopList());
        model.addAttribute("result",recomBoardService.getList(pageRequestDTO));
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/recom_register") //등록
    public void register(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model){
        //로그인 한 유저 정보 넘기기
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
    }
    @PostMapping("/recom_register") //등록
    public String registerPost(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model,RecomBoardDTO recomBoardDTO, RedirectAttributes redirectAttributes){
        Long bno=recomBoardService.register(recomBoardDTO);
        redirectAttributes.addFlashAttribute("msg",bno);
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
        return "redirect:/balloon/recom";
    }

    @GetMapping("/recom_read") //상세보기.수정화면
    public String read(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model, HttpServletRequest servletRequest, HttpServletResponse servletResponse){

        /* 조회수 로직 */
        Cookie oldCookie = null;
        Cookie[] cookies = servletRequest.getCookies();
        if(balloonAuthMemberDTO != null) {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("recom_" + balloonAuthMemberDTO.getId() + "_PostView")) {
                        oldCookie = cookie;
                    }
                }
            }

            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + bno.toString() + "]")) {
                    recomBoardService.updateHits(bno);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + bno + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24 * 365); // 쿠키 시간 1년
                    servletResponse.addCookie(oldCookie);
                }
            } else {
                recomBoardService.updateHits(bno);
                Cookie newCookie = new Cookie("recom_" + balloonAuthMemberDTO.getId() + "_PostView", "[" + bno.toString() + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24 * 365);                                // 쿠키 시간
                servletResponse.addCookie(newCookie);
            }
        } else{
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("recomPostView")) {
                        oldCookie = cookie;
                    }
                }
            }

            if (oldCookie != null) {
                if (!oldCookie.getValue().contains("[" + bno.toString() + "]")) {
                    recomBoardService.updateHits(bno);
                    oldCookie.setValue(oldCookie.getValue() + "_[" + bno + "]");
                    oldCookie.setPath("/");
                    oldCookie.setMaxAge(60 * 60 * 24 * 365); // 쿠키 시간 1년
                    servletResponse.addCookie(oldCookie);
                }
            } else {
                recomBoardService.updateHits(bno);
                Cookie newCookie = new Cookie("recomPostView", "[" + bno.toString() + "]");
                newCookie.setPath("/");
                newCookie.setMaxAge(60 * 60 * 24 * 365);                                // 쿠키 시간
                servletResponse.addCookie(newCookie);
            }
        }
        RecomBoardDTO recomboardDTO=recomBoardService.get(bno);
        model.addAttribute("memberDTO", balloonAuthMemberDTO);
        log.info(balloonAuthMemberDTO);
        model.addAttribute("dto",recomboardDTO);
        log.info(recomboardDTO);
        return "balloon/recom_read";
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/recom_modify") //상세보기.수정화면
    public String modify(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, PageRequestDTO pageRequestDTO, Long bno, Model model){
        RecomBoardDTO recomBoardDTO = recomBoardService.get(bno);

        System.out.println(recomBoardDTO);
        System.out.println(balloonAuthMemberDTO);

        if(recomBoardDTO.getId().equals(balloonAuthMemberDTO.getId())) {
            model.addAttribute("requestDTO", pageRequestDTO);
            model.addAttribute("dto", recomBoardDTO);

            return "balloon/recom_modify";
        }
        else{
            return "balloon/error";
        }
    }

    //삭제
//    @PostMapping("/free_remove")
//    public String remove(long fbno,RedirectAttributes redirectAttributes){
//        freeboardService.removeWithReplies(fbno);
//        redirectAttributes.addFlashAttribute("msg",fbno);
//        return "redirect:/balloon/free";
//    }
    @GetMapping("/recom_remove")
    public String remove(Long bno,RedirectAttributes redirectAttributes){
        recomBoardService.removeWithReplies(bno);
        return "redirect:/balloon/recom";
    }
    //수정
    @PostMapping("/recom_modify")
    public String modify(RecomBoardDTO dto, PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        recomBoardService.modify(dto);
        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
        redirectAttributes.addAttribute("bno",dto.getBno());

        return "redirect:/balloon/recom_read";
    }
}
