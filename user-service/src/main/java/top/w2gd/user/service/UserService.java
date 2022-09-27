package top.w2gd.user.service;

import top.w2gd.user.domain.dto.UserDto;
import top.w2gd.user.domain.entity.User;

/**
 * @Description TODO
 * @Date 2022-09-06-15-52
 * @Author qianzhikang
 */

public interface UserService {
    /**
     * 根据id查用户
     * @param id id
     * @return User
     */
    User findById(Integer id);

    /**
     * 用户登陆
     * @param userDto 登陆信息
     * @return User
     */
    User login(UserDto userDto);

}
