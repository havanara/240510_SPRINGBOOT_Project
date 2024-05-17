package com.ezen.www.controller;

import com.ezen.www.domain.CommentVO;
import com.ezen.www.domain.PagingVO;
import com.ezen.www.handler.PagingHandler;
import com.ezen.www.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;

@RestController
@RequestMapping("/comment/*")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService csv;

    @PostMapping("/post")
    @ResponseBody
    public String post(@RequestBody CommentVO cvo) {
        log.info(">> cvo >> {}", cvo);
        int isOK = csv.post(cvo);
        return isOK > 0 ? "1" : "0";
    }

    /*select * from comment odder by cno desc limit 0(pageStart),5(qty)*/
    @GetMapping("/list/{bno}/{page}")
    @ResponseBody
    public PagingHandler list(@PathVariable("bno") long bno, @PathVariable("page") int page){
        log.info(">> bno >> page >> {}" + bno + " / " + page);
        PagingVO pgvo = new PagingVO(page, 5);

        //Comment List
        //totalCount
        PagingHandler ph = csv.getList(bno, pgvo);
        return ph;
    }

    @ResponseBody
    @PutMapping("/modify")
    public String modify(@RequestBody CommentVO cvo){
        log.info(">> cvo >> {}", cvo);
        int isOK = csv.modify(cvo);
        return isOK > 0 ? "1":"0";
    }

    @ResponseBody
    @DeleteMapping("/delete/{cno}")
    public String delete(@PathVariable("cno") long cno){
        log.info(">> cno >> {}", cno);
        int isOK = csv.delete(cno);
        return isOK > 0 ? "1":"0";
    }
}
