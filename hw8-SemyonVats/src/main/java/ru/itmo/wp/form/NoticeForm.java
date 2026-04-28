package ru.itmo.wp.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NoticeForm {
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 1000, message = "Content must be less than 1000 characters")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}