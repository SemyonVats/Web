package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.security.AnyRole;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class PostPage extends Page {
    private final PostService postService;
    private final CommentService commentService;

    public PostPage(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/post/{id}")
    public String post(@PathVariable long id, Model model, HttpSession session) {
        Optional<Post> postOptional = postService.findByIdWithTags(id);
        if (!postOptional.isPresent()) {
            return "redirect:/";
        }

        Post post = postOptional.get();
        model.addAttribute("post", post);
        model.addAttribute("comment", new Comment());

        return "post";
    }

    @PostMapping("/post/{id}/comment")
    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    public String addComment(
            @PathVariable long id,
            @Valid Comment comment,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        User user = getUser(session);

        if (user == null) {
            putMessage(session, "You need to login to add comments");
            return "redirect:/enter";
        }

        Optional<Post> postOptional = postService.findById(id);
        if (!postOptional.isPresent()) {
            return "redirect:/";
        }

        Post post = postOptional.get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            model.addAttribute("comment", comment);
            return "post";
        }

        commentService.save(comment, post, user);

        return "redirect:/post/" + id;
    }
}