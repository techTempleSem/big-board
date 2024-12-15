package com.fastcaompus.boardserver.mapper;

import com.fastcaompus.boardserver.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    public int register(PostDTO postDTO);
    public List<PostDTO> selectMyPosts(int accountId);
    public int updatePosts(PostDTO postDTO);
    public int deletePosts(int postId);
}
