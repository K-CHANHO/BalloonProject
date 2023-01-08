package org.zerock.balloon.controller.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.balloon.dto.community.CommunityBoardReplyDTO;
import org.zerock.balloon.service.community.CommunityBoardReplyService;

import java.util.List;

@RestController
@RequestMapping("/replies/")
public class CommunityBoardReplyController {

    @Autowired
    private CommunityBoardReplyService communityBoardReplyService;

    @GetMapping(value = "/balloon/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommunityBoardReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){

        return new ResponseEntity<>(communityBoardReplyService.getList(bno), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody CommunityBoardReplyDTO communityBoardReplyDTO){

        communityBoardReplyService.register(communityBoardReplyDTO);

        return new ResponseEntity<>(0L,HttpStatus.OK);

    }

    @PostMapping("/modify")
    public ResponseEntity<Long> modify(@RequestBody CommunityBoardReplyDTO communityBoardReplyDTO){


        communityBoardReplyService.modify(communityBoardReplyDTO);

        return new ResponseEntity<>(0L,HttpStatus.OK);

    }

    @DeleteMapping("/remove/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){

        communityBoardReplyService.remove(rno);

        return new ResponseEntity<>("", HttpStatus.OK);
    }


}
