package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.repository.PostRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataInitService {
    private final PostRepository postRepository;
    private final TagService tagService;

    public DataInitService(PostRepository postRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    @PostConstruct
    @Transactional
    public void initializeTags() {
        try {
            List<Tag>tagList = new ArrayList<>();
            tagList.add(tagService.findOrCreateTag("gex"));
            tagList.add(tagService.findOrCreateTag("nya"));
            tagList.add(tagService.findOrCreateTag("OOO"));

            List<Post> posts = postRepository.findAllWithTags();

            for (Post post : posts) {
                Set<Tag> currentTags = post.getTags();
                int ind = (int) (Math.random() * tagList.size());
                if (!containsTag(currentTags, tagList.get(ind))) {
                    currentTags.add(tagList.get(ind));
                }
                postRepository.save(post);
            }
            System.err.println("Теги успешно инициализированы");
        } catch (Exception e) {
            System.err.println("Ошибка при инициализации тегов: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean containsTag(Set<Tag> tags, Tag tag) {
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet())
                .contains(tag.getName());
    }
}