package com.fastcaompus.boardserver.controller;

import com.fastcaompus.boardserver.aop.LoginCheck;
import com.fastcaompus.boardserver.dto.PostDTO;
import com.fastcaompus.boardserver.dto.UserDTO;
import com.fastcaompus.boardserver.dto.response.CommonResponse;
import com.fastcaompus.boardserver.service.UserService;
import com.fastcaompus.boardserver.service.impl.PostServiceImpl;
import com.fastcaompus.boardserver.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Log4j2
public class PostController {
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;


    public PostController(UserServiceImpl userService, PostServiceImpl postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDTO>> registerPost(
            String accountId,
            @RequestBody PostDTO postDTO
    ){
        postService.register(accountId, postDTO);
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "registerPost", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("my-posts")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<List<PostDTO>>> myPostInfo(String accountId){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        List<PostDTO> postDTOList = postService.selectMyPosts(memberInfo.getId());
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "myPostInfo", postDTOList);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostResponse>> updatePosts(
            String accountId,
            @PathVariable(name = "postId") int postId,
            @RequestBody PostRequest postRequest
    ){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        PostDTO postDTO = PostDTO.builder()
                .id(postId)
                .name(postRequest.getName())
                .contents(postRequest.getContents())
                .views(postRequest.getViews())
                .categoryId(postRequest.getCategoryId())
                .userId(postRequest.getUserId())
                .fileId(postRequest.getFileId())
                .updateTime(new Date()).build();
        postService.updatePosts(postDTO);

        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "updatePosts", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDeleteRequest>> deletePost(
            String accountId,
            @PathVariable(name = "postId") int postId,
            @RequestBody PostDeleteRequest postDeleteRequest
    ){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        postService.deletePosts(memberInfo.getId(), postId);

        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "SUCCESS", "deletePost", postDeleteRequest);
        return ResponseEntity.ok(commonResponse);
    }

    // --request--
    @Setter
    @Getter
    private static class PostRequest{
        private String name;
        private String contents;
        private int views;
        private int categoryId;
        private int userId;
        private int fileId;
        private Date updateTime;
    }

    @Setter
    @Getter
    private static class PostDeleteRequest{
        private int id;
        private int accountId;
    }

    // --response--
    @Getter
    @AllArgsConstructor
    private static class PostResponse{
        private List<PostDTO> postDTOs;
    }
}
