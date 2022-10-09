package top.w2gd.user.controller;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.w2gd.user.auth.CheckLogin;
import top.w2gd.user.common.ResponseResult;
import top.w2gd.user.common.ResultCode;
import top.w2gd.user.domain.entity.User;
import top.w2gd.user.domain.entity.dto.UserDto;
import top.w2gd.user.domain.vo.LoginVo;
import top.w2gd.user.service.UserService;
import top.w2gd.user.utils.JwtOperator;

import java.util.HashMap;

/**
 * @author w2gd
 */
@RestController
@Slf4j
@RequestMapping(value = "/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    public final UserService userService;
    private final JwtOperator jwtOperator;

    @GetMapping("{id}")
    @CheckLogin
    public ResponseResult getUserById(@PathVariable Integer id) {

        val user  = userService.findById(id);
        if (user != null) {
            return ResponseResult.success(user);
        } else {
            return ResponseResult.failure(ResultCode.RESULT_CODE_DATA_NONE);
        }
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserDto userDto) {
        User user = userService.login(userDto);
        if (user == null){
            return ResponseResult.failure(ResultCode.USER_ACCOUNT_ERROR,"账号或密码错误");
        }
        HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
        objectObjectHashMap.put("id", user.getId());
        objectObjectHashMap.put("role", user.getRoles());
        String token = jwtOperator.generateToken(objectObjectHashMap);
        return ResponseResult.success(LoginVo.builder().id(user.getId()).token(token).build());
    }
}
