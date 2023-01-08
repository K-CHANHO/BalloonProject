package org.zerock.balloon.controller.main;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.balloon.dto.PageRequestDTO;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;
import org.zerock.balloon.service.community.CommunityBoardService;
import org.zerock.balloon.service.free.FreeBoardService;
import org.zerock.balloon.service.qna.QnaBoardService;
import org.zerock.balloon.service.recom.RecomBoardService;

@Controller
@RequestMapping("/balloon/")
@Log4j2
@RequiredArgsConstructor
public class MainBoardController {
    private final FreeBoardService freeBoardService;    //자유게시판
    private final CommunityBoardService communityBoardService;   //커뮤니티게시판
    private final QnaBoardService qnaboardService;   //qna게시판
    private final RecomBoardService recomboardService;   //추천게시판
    @GetMapping("/main") //목록
    public void list(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, PageRequestDTO pageRequestDTO, Model model){

        model.addAttribute("memberDTO", balloonAuthMemberDTO);

        model.addAttribute("recomGoodTop", recomboardService.getTopList());
        model.addAttribute("recomTop", recomboardService.getTopFive());

        model.addAttribute("communityGoodTop", communityBoardService.getTopList()); // 좋아요 상위 3개
        model.addAttribute("communityTop", communityBoardService.getTopFive()); // 최신글 상위 5개

        model.addAttribute("freeTop", freeBoardService.getTopFive());

        model.addAttribute("qnaTop", qnaboardService.getTopFive());

    }

}

