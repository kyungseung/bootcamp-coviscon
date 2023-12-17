package com.coviscon.postservice.controller;

import com.coviscon.postservice.dto.request.RequestCommentCreate;
import com.coviscon.postservice.dto.response.ResponseCommentList;
import com.coviscon.postservice.dto.response.ResponsePostEdit;
import com.coviscon.postservice.service.CommentService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
//@RequestMapping("/post-service")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /*
     *   comment 기존 값을 수정페이지로 넘겨줌
     */
    @GetMapping("/comment/{commentId}/edit")
    public String updateComment(@PathVariable Long commentId, Model model) {
        try {
            ResponseCommentList responseCommentList = commentService.modifyCommentById(commentId);
            model.addAttribute("modifyComments", responseCommentList);
            return "post/detail";

        } catch (NoSuchElementException e) {
            return "post/list";
        }
    }

}
