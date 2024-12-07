package com.fastcaompus.boardserver.mapper;

import com.fastcaompus.boardserver.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {
    public UserDTO getUserProfile(@Param("id") String id);

    int insertUserProfile(@Param("id") String id, @Param("password") String password,
                          @Param("name") String name, @Param("phone") String phone,
                          @Param("address") String address);

    int updateUserProfile(@Param("id") String id, @Param("password") String password,
                          @Param("name") String name, @Param("phone") String phone,
                          @Param("address") String address);

    int deleteUserProfile(@Param("id") String id);

    int register(UserDTO userProfile);

    public UserDTO findByIdAndPassword(@Param("id") String id,
                                       @Param("password") String password);

    public UserDTO findByUserIdAndPassword(@Param("userId") String userId,
                                       @Param("password") String password);

    int idCheck(String userId);

    public int updatePassword(UserDTO user);
    public int updateAddress(UserDTO user);
}
