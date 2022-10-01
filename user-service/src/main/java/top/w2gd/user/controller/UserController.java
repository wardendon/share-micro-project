package top.w2gd.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.w2gd.user.common.ResponseResult;
import top.w2gd.user.common.ResultCode;
import top.w2gd.user.domain.entity.User;
import top.w2gd.user.domain.entity.dto.UserDto;
import top.w2gd.user.service.UserService;

/**
 * @author w2gd
 */
@RestController
@Slf4j
@RequestMapping(value = "/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    public final UserService userService;

    @GetMapping("{id}")
    public ResponseResult getUserById(@PathVariable Integer id) {
        // try {
        //     Thread.sleep(3000);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
        val user  = userService.findById(id);
        if (user != null) {
            return ResponseResult.success(user);
        } else {
            return ResponseResult.failure(ResultCode.RESULT_CODE_DATA_NONE);
        }
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserDto userDto) {
        return ResponseResult.success(userService.login(userDto));
    }
}
