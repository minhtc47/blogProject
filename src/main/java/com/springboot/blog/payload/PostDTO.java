package com.springboot.blog.payload;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostDTO {
    private long id;
    @NotEmpty
    @Size(min = 2,message = "Post title must have at least 2 characters")
    private String title;

    @NotEmpty
    @Size(min = 10,message = "Post description must have at least 10 characters")
    private String description;

    @NotEmpty
    private String content;
    private Set<CommentDto> commentsSet;
}
