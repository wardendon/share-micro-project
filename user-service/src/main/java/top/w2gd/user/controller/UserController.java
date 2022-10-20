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
import top.w2gd.user.domain.entity.dto.UserProfileAuditDto;
import top.w2gd.user.domain.vo.BonusVo;
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
    @CheckLogin // 使用登录检查后，openfeign 调用的服务如果没有传token过来,就会调用失败
    public ResponseResult getUserById(@PathVariable Integer id) {

        val user  = userService.findById(id);
        if (user != null) {
            return ResponseResult.success(user);
        } else {
            return ResponseResult.failure(ResultCode.RESULT_CODE_DATA_NONE);
        }
    }

    /**
     * 获取积分
     * @param id id
     * @return bonus
     */
    @GetMapping("/bonus")
    public ResponseResult getBonus(@RequestParam int id){
        val user = userService.findById(id);
        if (user == null){
            return ResponseResult.failure(ResultCode.PARAM_IS_INVALID,"未查到该用户");
        }
        return  ResponseResult.success(BonusVo.builder().id(user.getId()).bonus(user.getBonus()).build());
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

    /**
     * 修改个人信息接口
     * @param userProfileAuditDto 新的个人信息
     * @return 修改的后的用户信息
     */
    @CheckLogin
    @PostMapping("/update")
    public ResponseResult auditProfile(@RequestBody UserProfileAuditDto userProfileAuditDto){
        User user = userService.auditProfile(userProfileAuditDto);
        if (user == null){
            return ResponseResult.failure(ResultCode.PARAM_IS_BLANK);
        }
        return ResponseResult.success(user);
    }
}
