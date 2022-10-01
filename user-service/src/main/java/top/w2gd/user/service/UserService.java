package top.w2gd.user.service;

import top.w2gd.user.domain.entity.User;
import top.w2gd.user.domain.entity.dto.UserDto;

/**
 * @author w2gd
 */
public interface UserService {
    /**
     *  根据id找用户
     * @param id id
     * @return user
     */
    User findById(Integer id);

    /**
     * 用户登录方法
     * @param userDto
     * @return
     */
    User login(UserDto userDto);
}
