package com.fastcaompus.boardserver.service.impl;

import com.fastcaompus.boardserver.dto.UserDTO;
import com.fastcaompus.boardserver.exception.DuplicateIdException;
import com.fastcaompus.boardserver.mapper.UserProfileMapper;
import com.fastcaompus.boardserver.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.fastcaompus.boardserver.utils.SHA256Util.encryptSHA256;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserProfileMapper userProfilMapper;
    public UserServiceImpl(UserProfileMapper userProfileMapper){
        this.userProfilMapper = userProfileMapper;
    }
    @Override
    public void register(UserDTO userProfile) {
        boolean dupleIdResult = isDuplicatedId(userProfile.getUserId());
        if (dupleIdResult){
            throw new DuplicateIdException("중복된 아이디");
        }
        userProfile.setCreateTime(new Date());
        userProfile.setPassword(encryptSHA256(userProfile.getPassword()));
        int insertCount = userProfilMapper.register(userProfile);

        if(insertCount != 1){
            log.error("insert ERROR! {}",userProfile);
            throw new RuntimeException(
                    "insertUser ERROR! 회원가입 메서드 확인");
        }
    }

    @Override
    public UserDTO login(String id, String password) {
        String cryptoPassword = encryptSHA256(password);
        UserDTO memberInfo = userProfilMapper.findByUserIdAndPassword(id, cryptoPassword);
        return memberInfo;
    }

    @Override
    public boolean isDuplicatedId(String id) {
        return userProfilMapper.idCheck(id) == 1;
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        UserDTO userDTO = userProfilMapper.getUserProfile(userId);
        return userDTO;
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String cryptoPassword = encryptSHA256(beforePassword);
        log.info(id);
        log.info(cryptoPassword);
        UserDTO memberInfo = userProfilMapper.findByUserIdAndPassword(id, cryptoPassword);

        if(memberInfo != null){
            memberInfo.setPassword(encryptSHA256(afterPassword));
            int insertCount = userProfilMapper.updatePassword(memberInfo);
        } else {
            log.error("updatePassword ERROR : {}",memberInfo);
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String cryptoPassword = encryptSHA256(password);
        UserDTO memberInfo = userProfilMapper.findByUserIdAndPassword(id, cryptoPassword);

        if(memberInfo != null){
            int deleteCount = userProfilMapper.deleteUserProfile(id);
        } else {
            log.error("delete ERROR : {}",memberInfo);
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
