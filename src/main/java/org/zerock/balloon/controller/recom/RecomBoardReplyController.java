package org.zerock.balloon.controller.recom;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.balloon.dto.recom.RecomBoardReplyDTO;
import org.zerock.balloon.service.recom.RecomBoardReplyService;

import java.util.List;

@RestController
@RequestMapping("/recom_replies/")
@Log4j2
@RequiredArgsConstructor
public class RecomBoardReplyController {
    @Autowired
    private RecomBoardReplyService recomBoardReplyService;

    @GetMapping(value = "/balloon/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecomBoardReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){

        return new ResponseEntity<>(recomBoardReplyService.getList(bno), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody RecomBoardReplyDTO recomBoardReplyDTO){

        recomBoardReplyService.register(recomBoardReplyDTO);

        return new ResponseEntity<>(0L,HttpStatus.OK);

    }

    @PostMapping("/modify")
    public ResponseEntity<Long> modify(@RequestBody RecomBoardReplyDTO recomBoardReplyDTO){


        recomBoardReplyService.modify(recomBoardReplyDTO);

        return new ResponseEntity<>(0L,HttpStatus.OK);

    }

    @DeleteMapping("/remove/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){

        recomBoardReplyService.remove(rno);

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
