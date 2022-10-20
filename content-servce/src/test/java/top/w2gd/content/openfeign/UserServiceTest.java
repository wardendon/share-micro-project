package top.w2gd.content.openfeign;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import top.w2gd.content.common.ResponseResult;
import top.w2gd.content.domain.dto.UserProfileAuditDto;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Resource
    UserService userService;

    @Test
    void auditUserData() {
        ResponseResult responseResult = userService.auditUserData(UserProfileAuditDto.builder().id(1).bonus(100).build(), "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJpZCI6MSwiaWF0IjoxNjY2Mjc5NjYzLCJleHAiOjE2Njc0ODkyNjN9.fluvsDuuGRzLOwEINGIKafHsAAHZrkwiEu-XbpIQmdQ");
        System.out.println(responseResult.getData());
    }
}