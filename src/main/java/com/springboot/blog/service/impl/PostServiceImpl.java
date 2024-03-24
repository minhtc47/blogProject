package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repositoty.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    PostRepository postRepository;

    ModelMapper mapper;
    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = mapToEntity(postDTO);
        Post newPost = postRepository.save(post);
        return mapToDTO(newPost);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy, String sortDir) {
        //Create pageable instance
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageRequest = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageRequest);

        //Get content from pageable
        List<Post> listOfPosts = posts.getContent();
        List<PostDTO> content = posts.stream().map(this::mapToDTO).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(pageNo);
        postResponse.setPageSize(pageSize);
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPaged(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
//        return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        return postResponse;
    }

    @Override
    public PostDTO getPostById(long id) {
        String sid = String.valueOf(id);
        return mapToDTO(postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",sid)));
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        String sid = String.valueOf(id);
        Post post =postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",sid));
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());
        return mapToDTO(postRepository.save(post));
    }

    @Override
    public void deletePostById(long id) {
        String sid = String.valueOf(id);
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", sid));
        postRepository.delete(post);
    }

    private PostDTO mapToDTO(Post post){
        PostDTO postDto = mapper.map(post,PostDTO.class);
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }
    private Post mapToEntity(PostDTO postDto){
        Post post = mapper.map(postDto,Post.class);
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}
