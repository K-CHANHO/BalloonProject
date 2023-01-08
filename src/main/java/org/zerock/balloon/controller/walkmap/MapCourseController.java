package org.zerock.balloon.controller.walkmap;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.balloon.dto.walkmap.MapCourseDTO;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;
import org.zerock.balloon.service.walkmap.MapCourseService;

import java.net.URLEncoder;

@Controller
@RequestMapping("/balloon/")
@Log4j2
@RequiredArgsConstructor
public class MapCourseController {
        private final MapCourseService mapCourseService;



        @GetMapping(value = "/walk_map")
        public String map(@AuthenticationPrincipal BalloonAuthMemberDTO balloonAuthMemberDTO, Model model, String keyword){
            model.addAttribute("pindto",mapCourseService.getPin());
            model.addAttribute("memberDTO", balloonAuthMemberDTO);
            model.addAttribute("keyword", keyword);
            System.out.println(">>>>>>>>>>>>>"+ keyword);
            return "/balloon/walk_map";
        }

        @GetMapping("/walk_map/{mno}")
        public ResponseEntity<MapCourseDTO> read(@PathVariable("mno") Long mno){
            MapCourseDTO mapCourseDTO=mapCourseService.get(mno);
            log.info(mapCourseDTO + "************************************");
            log.info("#!@#!%%$!@$!@!@@!#!@#!@");
            return new ResponseEntity<>(mapCourseDTO, HttpStatus.OK);
        }

}
