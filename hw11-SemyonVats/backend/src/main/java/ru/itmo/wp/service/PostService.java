package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.dto.PostDto;
import ru.itmo.wp.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDto findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return convertToDto(post);
    }

    public List<PostDto> findAllDto() {
        return postRepository.findAllByOrderByCreationTimeDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    public PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());
        dto.setCreationTime(post.getCreationTime());

        if (post.getUser() != null) {
            dto.setAuthor(post.getUser().getLogin());
        } else {
            dto.setAuthor("unknown");
        }

        return dto;
    }
}