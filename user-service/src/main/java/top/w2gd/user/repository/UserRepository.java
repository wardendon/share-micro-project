package top.w2gd.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.w2gd.user.domain.entity.User;

/**
 * @author w2gd
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    /**
     * 根据手机号和密码查找用户
     * @param mobile 手机号
     * @param password 密码
     * @return 用户
     */
    User findByMobileAndPassword(String mobile, String password);
}
