package top.w2gd.content.openfeign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.w2gd.content.common.ResponseResult;
import top.w2gd.content.domain.entity.User;
import top.w2gd.content.openfeign.UserService;

/**
 * @author w2gd
 */
@Slf4j
@Component
public class UserServiceFallback implements UserService {

    @Override
    public ResponseResult getUser(int id) {
        log.info("fallback getUser");
        User user = User.builder().id(4312).mobile("123456").nickname("降级方案返回用户").build();
        return ResponseResult.success(user);
    }
}
