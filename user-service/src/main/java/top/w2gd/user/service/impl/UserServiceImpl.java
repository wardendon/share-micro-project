package top.w2gd.user.service.impl;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.w2gd.user.domain.entity.User;
import top.w2gd.user.domain.entity.dto.UserDto;
import top.w2gd.user.repository.UserRepository;
import top.w2gd.user.service.UserService;
import top.w2gd.user.utils.JwtOperator;

import java.util.HashMap;

/**
 * @author w2gd
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtOperator jwtOperator;

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User login(UserDto userDto) {
        User user = userRepository.findByMobileAndPassword(userDto.getMobile(), userDto.getPassword());
        if (user != null){
            HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
            objectObjectHashMap.put("id", user.getId());
            objectObjectHashMap.put("wxNickname", user.getNickname());
            objectObjectHashMap.put("role", user.getRoles());
            String token = jwtOperator.generateToken(objectObjectHashMap);
            log.info(token);
        }
        return user;
    }
}
