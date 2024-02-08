package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repositoty.CommentRepository;
import com.springboot.blog.repositoty.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        //retrieve post entity bt id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",String.valueOf(postId)));

        //set post to comment entity
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",String.valueOf(postId)));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",String.valueOf(commentId)));

        if(comment.getPost().getId() != post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",String.valueOf(postId)));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",String.valueOf(commentId)));

        if(comment.getPost().getId() != post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        Comment updateComment = commentRepository.save(comment);
        return mapToDto(updateComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",String.valueOf(postId)));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",String.valueOf(commentId)));

        if(comment.getPost().getId() != post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        return comment;
    }

}
