package org.zerock.balloon.controller.free;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.balloon.dto.free.FreeBoardReplyDTO;
import org.zerock.balloon.service.free.FreeBoardReplyService;

import java.util.List;

@RestController
@RequestMapping("/free_replies/")
public class FreeBoardReplyController {

    @Autowired
    private FreeBoardReplyService freeBoardReplyService;

    @GetMapping(value = "/balloon/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FreeBoardReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){

        return new ResponseEntity<>(freeBoardReplyService.getList(bno), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody FreeBoardReplyDTO freeBoardReplyDTO){

        freeBoardReplyService.register(freeBoardReplyDTO);

        return new ResponseEntity<>(0L,HttpStatus.OK);

    }

    @PostMapping("/modify")
    public ResponseEntity<Long> modify(@RequestBody FreeBoardReplyDTO freeBoardReplyDTO){


        freeBoardReplyService.modify(freeBoardReplyDTO);

        return new ResponseEntity<>(0L,HttpStatus.OK);

    }

    @DeleteMapping("/remove/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){

        freeBoardReplyService.remove(rno);

        return new ResponseEntity<>("", HttpStatus.OK);
    }


}
