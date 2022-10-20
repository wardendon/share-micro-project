package top.w2gd.content.openfeign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.w2gd.content.common.ResponseResult;
import top.w2gd.content.domain.dto.UserProfileAuditDto;
import top.w2gd.content.domain.entity.User;
import top.w2gd.content.openfeign.UserService;

/**
 * @author w2gd
 */
@Slf4j
@Component
public class UserServiceFallback implements UserService {


    @Override
    public ResponseResult getUser(Integer id,String token) {
        log.info("getUser fallback");
        //log.info(token);
        User user = User.builder().avatar("logo.png").nickname("降级方案用户").mobile("10000000000").build();
        return ResponseResult.success(user);
    }

    @Override
    public ResponseResult auditUserData(UserProfileAuditDto userProfileAuditDto, String token) {
        return null;
    }
}
