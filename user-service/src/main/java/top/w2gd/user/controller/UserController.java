package top.w2gd.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.w2gd.user.common.ResponseResult;
import top.w2gd.user.domain.dto.UserDto;
import top.w2gd.user.service.UserService;

/**
 * @Description TODO
 * @Date 2022-09-06-16-15
 * @Author qianzhikang
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    @GetMapping("{id}")
    @SentinelResource(value = "getUserById")
    public ResponseResult getUserById(@PathVariable Integer id){
        //try {
        //    Thread.sleep(3000);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
        //int cmd = 3/0;
        return ResponseResult.success(userService.findById(id));
    }

    @PostMapping("/login")
    public ResponseResult login(UserDto userDto){
        return ResponseResult.success(userService.login(userDto));
    }

}
