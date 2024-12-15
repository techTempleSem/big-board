package com.fastcaompus.boardserver.service.impl;

import com.fastcaompus.boardserver.dto.PostDTO;
import com.fastcaompus.boardserver.dto.UserDTO;
import com.fastcaompus.boardserver.mapper.PostMapper;
import com.fastcaompus.boardserver.mapper.UserProfileMapper;
import com.fastcaompus.boardserver.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public void register(String id, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        postDTO.setUserId(memberInfo.getId());
        postDTO.setCreateTime(new Date());

        if(memberInfo != null){
            postMapper.register(postDTO);
        } else {
            log.error("post register ERROR! {}", postDTO);
            throw new RuntimeException("post register ERROR! 게시글 등록 메서드 확인 "+postDTO);
        }
    }

    @Override
    public List<PostDTO> selectMyPosts(int accountId) {
        List<PostDTO> postDTOList = postMapper.selectMyPosts(accountId);
        return postDTOList;
    }

    @Override
    public void updatePosts(PostDTO postDTO) {
        if(postDTO != null && postDTO.getId() != 0){
            postMapper.updatePosts(postDTO);
        } else {
            log.error("post update ERROR! {}", postDTO);
            throw new RuntimeException("post update ERROR! 게시글 등록 메서드 확인 "+postDTO);
        }
    }

    @Override
    public void deletePosts(int userId, int postId) {
        if(userId != 0 && postId != 0){
            postMapper.deletePosts(postId);
        } else {
            log.error("post delete ERROR! {}", postId);
            throw new RuntimeException("post delete ERROR! 게시글 등록 메서드 확인 "+postId);
        }
    }
}
