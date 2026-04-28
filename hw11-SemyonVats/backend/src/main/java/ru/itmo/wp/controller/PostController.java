package ru.itmo.wp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.dto.PostDto;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.PostService;
import ru.itmo.wp.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final JwtService jwtService;

    public PostController(PostService postService, UserService userService, JwtService jwtService) {
        this.postService = postService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/posts/{id}")
    public PostDto getPostById(@PathVariable Long id) {
        return postService.findPostById(id);
    }


    @GetMapping("/posts")
    public List<PostDto> getAllPosts() {
        return postService.findAllDto();
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestBody Map<String, String> requestData,
            @RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authorizationHeader.substring(7).trim();

        String title = requestData.get("title");
        String text = requestData.get("text");

        if (title == null || title.trim().isEmpty() ||
                text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (title.length() > 60 || text.length() > 65000) {
            return ResponseEntity.badRequest().body(null);
        }

        User currentUser = jwtService.find(jwt);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Post post = new Post();
        post.setTitle(title.trim());
        post.setText(text.trim());
        post.setUser(currentUser);
        post.setCreationTime(new Date());

        Post savedPost = postService.save(post);
        PostDto postDto = postService.convertToDto(savedPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(postDto);
    }
}