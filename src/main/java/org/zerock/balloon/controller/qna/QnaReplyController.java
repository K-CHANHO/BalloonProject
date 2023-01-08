package org.zerock.balloon.controller.qna;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.balloon.dto.qna.QnaReplyDTO;
import org.zerock.balloon.service.qna.QnaReplyService;

import java.util.List;
@RestController
@RequestMapping("/qna_replies/")
@Log4j2
@RequiredArgsConstructor
public class QnaReplyController {
    private final QnaReplyService qnaReplyService;

    @GetMapping(value = "/balloon/{qbno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QnaReplyDTO>> getListByBoard(@PathVariable("qbno") Long qbno ){

        log.info("qbno: " + qbno);

        return new ResponseEntity<>( qnaReplyService.getList(qbno), HttpStatus.OK);

    }
    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody QnaReplyDTO qnaReplyDTO){

        log.info(qnaReplyDTO);

        Long qbrno = qnaReplyService.register(qnaReplyDTO);

        return new ResponseEntity<>(qbrno, HttpStatus.OK);
    }
    @DeleteMapping("/remove/{qbrno}")
    public ResponseEntity<String> remove(@PathVariable("qbrno") Long qbrno) {

        log.info("RNO:" + qbrno );

        qnaReplyService.remove(qbrno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/modify")
    public ResponseEntity<Long> modify(@RequestBody QnaReplyDTO qnaReplyDTO) {

        log.info(qnaReplyDTO);
        qnaReplyService.modify(qnaReplyDTO);
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return new ResponseEntity<>(0L, HttpStatus.OK);

    }
}
