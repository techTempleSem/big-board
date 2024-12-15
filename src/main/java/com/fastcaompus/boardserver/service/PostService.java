package com.fastcaompus.boardserver.service;

import com.fastcaompus.boardserver.dto.PostDTO;

import java.util.List;

public interface PostService {
    void register(String id, PostDTO postDTO);
    List<PostDTO> selectMyPosts(int accountId);
    void updatePosts(PostDTO postDTO);
    void deletePosts(int userId, int postId);
}
