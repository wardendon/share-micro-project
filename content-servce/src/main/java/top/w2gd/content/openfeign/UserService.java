package top.w2gd.content.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.w2gd.content.common.ResponseResult;
import top.w2gd.content.openfeign.fallback.UserServiceFallback;
import top.w2gd.content.openfeign.fallback.UserServiceFallbackFactory;

/**
 * @author w2gd
 */
@FeignClient(value = "user-service",path = "/users",fallbackFactory = UserServiceFallbackFactory.class)
// @FeignClient(value = "user-service",path = "/users",fallback = UserServiceFallback.class)
public interface UserService {
    /**
     * 调用用户服务
     * @param id id
     * @return User
     */
    @GetMapping("{id}")
    ResponseResult getUser(@PathVariable(value = "id") int id);

}
