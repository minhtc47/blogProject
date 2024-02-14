package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private long id;
    @NotEmpty(message = "Comment name should not be null or empty")
    private String name;
    @NotEmpty(message = "Comment email should not be null or empty")
    @Email
    private String email;
    @NotEmpty(message = "Body should not be null or empty")
    @Size(min = 10,message = "Comment body must have at least 10 characters")
    private String body;
}