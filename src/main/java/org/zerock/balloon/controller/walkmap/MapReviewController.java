package org.zerock.balloon.controller.walkmap;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.balloon.dto.community.CommunityBoardReplyDTO;
import org.zerock.balloon.dto.walkmap.MapReviewDTO;
import org.zerock.balloon.service.walkmap.MapReviewService;

import java.util.List;

@Controller
@RequestMapping("/reviews/")
@Log4j2
@RequiredArgsConstructor
public class MapReviewController {
    private final MapReviewService mapReviewService;
    @GetMapping(value = "/walk_map/{mno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MapReviewDTO>> Review(@PathVariable("mno") Long mno){
        return new ResponseEntity<>(mapReviewService.getReview(mno), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody MapReviewDTO mapReviewDTO){
        mapReviewService.register(mapReviewDTO);
        return new ResponseEntity<>(0L, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/modify")
    public ResponseEntity<Long> modify(@RequestBody MapReviewDTO mapReviewDTO){
        System.out.println(mapReviewDTO + "^^^^^^^^^^^^^^^^^^^^^^");
        mapReviewService.modify(mapReviewDTO);
        return new ResponseEntity<>(0L,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/remove/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){

        mapReviewService.remove(rno);

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
