package com.fastcaompus.boardserver.controller;

import com.fastcaompus.boardserver.aop.LoginCheck;
import com.fastcaompus.boardserver.dto.UserDTO;
import com.fastcaompus.boardserver.dto.request.UserDeleteIdRequest;
import com.fastcaompus.boardserver.dto.request.UserLoginRequest;
import com.fastcaompus.boardserver.dto.request.UserUpdatePasswordRequest;
import com.fastcaompus.boardserver.dto.response.LoginResponse;
import com.fastcaompus.boardserver.dto.response.UserInfoResponse;
import com.fastcaompus.boardserver.service.impl.UserServiceImpl;
import com.fastcaompus.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {
    private final UserServiceImpl userService;
    private static LoginResponse loginResponse = null;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDTO){
        if(UserDTO.hasNullDataBeforeRegister(userDTO)){
            throw new RuntimeException("회원가입 정보 확인!");
        }
        userService.register(userDTO);
    }

    @PostMapping("/sign-in")
    public HttpStatus signIn(@RequestBody UserLoginRequest userLoginRequest,
                             HttpSession session){
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = userLoginRequest.getUserId();
        String password = userLoginRequest.getPassword();
        UserDTO userInfo = userService.login(id, password);

        if(userInfo == null){
            return HttpStatus.NOT_FOUND;
        }
        else {
            loginResponse = LoginResponse.success(userInfo);
            if(userInfo.getStatus() == UserDTO.Status.ADMIN){
                SessionUtil.setLoginAdminId(session, id);
            } else {
                SessionUtil.setLoginMemberId(session, id);
            }
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        }
        return HttpStatus.OK;
    }

    @GetMapping("/my-info")
    public UserInfoResponse memberInfo(HttpSession session){
        String id = SessionUtil.getLoginMemberId(session);
        if(id == null){
            id = SessionUtil.getLoginAdminId(session);
        }
        UserDTO memberInfo = userService.getUserInfo(id);
        return new UserInfoResponse(memberInfo);
    }

    @PutMapping("/logout")
    public void logout (HttpSession session){
        SessionUtil.clear(session);
    }

    @PatchMapping("/password")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> updateUserPassword(
            String accountId,
            @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
            HttpSession session
    ){
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = accountId;
        log.info("{}",accountId);
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassword(id, beforePassword, afterPassword);
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        } catch(Exception e){
            log.error("변경 실패", e);
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteIdRequest userDeleteIdRequest, HttpSession session){
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = SessionUtil.getLoginMemberId(session);
        try{
            userService.deleteId(id, userDeleteIdRequest.getPassword());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (Exception e){
            log.error("삭제 실패");
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
