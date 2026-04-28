package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.repository.CommentRepository;

import java.util.Date;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void save(Comment comment, Post post, User user) {
        comment.setPost(post);
        comment.setUser(user);

        if (comment.getCreationTime() == null) {
            comment.setCreationTime(new Date());
        }

        commentRepository.save(comment);
    }
}