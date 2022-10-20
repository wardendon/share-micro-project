package top.w2gd.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.w2gd.user.domain.entity.BonusEventLog;
import top.w2gd.user.domain.entity.User;
import top.w2gd.user.domain.entity.dto.UserDto;
import top.w2gd.user.domain.entity.dto.UserProfileAuditDto;
import top.w2gd.user.repository.BonusEventLogRepository;
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

    private final BonusEventLogRepository bonusEventLogRepository;

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

    /**
     * 修改个人信息
     *
     * @param userProfileAuditDto 新的用户个人信息
     * @return 新用户信息
     */
    @Override
    public User auditProfile(UserProfileAuditDto userProfileAuditDto) {
        User user = userRepository.findById(userProfileAuditDto.getId()).orElse(null);
        if (user != null) {
            // hutools 赋值转换工具
            BeanUtil.copyProperties(userProfileAuditDto,user, CopyOptions.create().ignoreNullValue());
            log.info(user.toString());
            return userRepository.saveAndFlush(user);
        } else {
            return null;
        }

    }

    /**
     * 获取积分明细
     *
     * @param userId   用户id
     * @param pageNum  分页
     * @param pageSize 分页
     * @return 分页积分明细
     */
    @Override
    public Page<BonusEventLog> getBonusRecord(Integer userId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createTime").descending());
        return bonusEventLogRepository.findByUserId(userId,pageable);
    }
}
