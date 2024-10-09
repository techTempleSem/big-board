package com.fastcaompus.boardserver.mapper;

import com.fastcaompus.boardserver.dto.UserDTO;
import org.apache.ibatis.annotations.Param;

public interface UserProfileMapper {

    int deleteUserProfile(@Param("id") String id);

    public UserDTO findByIdAndPassword(@Param("id") String id,
                                       @Param("password") String password);

    int idCheck(String userId);

    public int updatePassword(UserDTO user);

    int register(UserDTO userProfile);
}
