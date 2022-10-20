package top.w2gd.content.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import top.w2gd.content.common.ResponseResult;
import top.w2gd.content.domain.dto.AuditShareDto;
import top.w2gd.content.domain.dto.ShareQueryDto;
import top.w2gd.content.domain.dto.UserAddBounsDto;
import top.w2gd.content.domain.dto.UserProfileAuditDto;
import top.w2gd.content.domain.entity.BonusEventLog;
import top.w2gd.content.domain.entity.MidUserShare;
import top.w2gd.content.domain.entity.Share;
import top.w2gd.content.domain.entity.User;
import top.w2gd.content.domain.enums.ShareAuditEnums;
import top.w2gd.content.openfeign.UserService;
import top.w2gd.content.repository.BonusEventLogRepository;
import top.w2gd.content.repository.MidUserShareRepository;
import top.w2gd.content.repository.ShareRepository;
import top.w2gd.content.service.MidUserShareService;
import top.w2gd.content.service.ShareService;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author w2gd
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor =  @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareRepository shareRepository;

    private final MidUserShareService midUserShareService;

    private  final  MidUserShareRepository midUserShareRepository;

    private final RocketMQTemplate rocketMQTemplate;

    private final UserService userService;

    private final BonusEventLogRepository bonusEventLogRepository;
    @Override
    public Share findById(Integer id) {
        return shareRepository.findById(id).orElse(null);
    }

    @SentinelResource(value = "getNumber",blockHandler = "blockHandlerGetNumber")
    @Override
    public String getNumber(int number) {
        return "number = {"+ number +"}";
    }

    /**
     * 获取所有资源
     * 可按条件查询
     * @return List
     */
    @Override
    public Page<Share> getAll(int pageNum, int pageSize, ShareQueryDto shareQueryDto, Integer userId) {
        // 分页规则
        Pageable pageable = PageRequest.of(pageNum, pageSize,Sort.by(Sort.Direction.DESC,"createTime"));
        // 条件查询
        Page<Share> all = shareRepository.findAll(new Specification<Share>() {
            @Override
            public Predicate toPredicate(Root<Share> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                // 是否显示条件
                predicates.add(criteriaBuilder.equal(root.get("showFlag").as(Integer.class), 1));
                // 处理查询封装对象空值问题
                if (shareQueryDto != null) {
                    // 拼接条件
                    if (shareQueryDto.getTitle() != null && !shareQueryDto.getTitle().equals("")) {
                        predicates.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + shareQueryDto.getTitle() + "%"));
                    }
                    if (shareQueryDto.getSummary() != null && !shareQueryDto.getSummary().equals("")) {
                        predicates.add(criteriaBuilder.like(root.get("summary").as(String.class), "%" + shareQueryDto.getSummary() + "%"));
                    }
                    if (shareQueryDto.getAuthor() != null && !shareQueryDto.getAuthor().equals("")) {
                        predicates.add(criteriaBuilder.like(root.get("author").as(String.class), "%" + shareQueryDto.getAuthor() + "%"));
                    }
                }
                return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[predicates.size()])));
            }
        }, pageable);
        if (userId == null){
            all.forEach(share -> share.setDownloadUrl(""));
        }else {
            all.forEach(share -> {
                Integer shareId = share.getId();
                MidUserShare midUserShare = midUserShareService.selectRecordWithUserIdAndShareId(userId, shareId);
                if (midUserShare == null) {
                    share.setDownloadUrl("");
                }
            });
        }
        return all;
    }

    /**
     * 审核分享内容
     *
     * @param auditShareDto 分享内容dto
     * @return 分享内容详情
     */
    @Override
    public Share auditShare(AuditShareDto auditShareDto) {
        Share share = shareRepository.findById(auditShareDto.getId()).orElse(null);
        assert share != null;
        // if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
        //     throw new IllegalArgumentException("参数非法！该分享已审核！");
        // }
        share.setAuditStatus(auditShareDto.getShareAuditEnums().toString());
        share.setReason(auditShareDto.getReason());
        share.setShowFlag(auditShareDto.getShowFlag() ? 1 : 0);

        Share newShare = shareRepository.saveAndFlush(share);
        // 向中件表插入数据
        midUserShareService.insert(
                MidUserShare.builder()
                        .shareId(newShare.getId())
                        .userId(newShare.getUserId())
                        .build()

        );

        // 如果是PASS,那么发送消息给rocketmq,让用户中心去消费，并为发布人添加积分
        if(ShareAuditEnums.PASS.equals(auditShareDto.getShareAuditEnums())) {
            rocketMQTemplate.convertAndSend(
                    "add-bonus",
                    UserAddBounsDto.builder()
                            .userId(share.getUserId())
                            .bonus(50)
                            .build());

        }
        return share;
    }

    @Override
    public String blockHandlerGetNumber(int number, BlockException e) {
        return "BLOCKED";
    }

    /**
     * 获取分页资源
     *
     * @param pageNum  当前页
     * @param pageSize 每页数量
     * @return 分页数据
     */
    @Override
    public Page<Share> getPageShare(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.by("createTime").descending());
        return shareRepository.findByShowFlag(1,pageRequest);
    }

    /**
     *
     * @param pageNum 当前页
     * @param pageSize 每页数量
     * @param status 审核状态
     * @return 分页数据
     */
    @Override
    public Page<Share> getPageShareByAudit(int pageNum, int pageSize, String status) {
        PageRequest pageRequest = PageRequest.of(pageNum,pageSize,Sort.by("createTime").descending());
        return shareRepository.findByAuditStatus(status,pageRequest);
    }

    /**
     * 根据用户ID 返回shares
     * @param userId 用户ID
     * @return .
     */
    @Override
    public List<Share> getSharesByUserId(Integer userId) {

        return shareRepository.findAllByUserId(userId,Sort.by("createTime").descending());
    }

    @Override
    public Share addShare(Share share) {
        return shareRepository.saveAndFlush(share);
    }

    /**
     * 兑换资源
     *
     * @param shareId 资源id
     * @param userId  用户id
     * @return 兑换的资源
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Share exchange(Integer shareId, Integer userId, String token) throws Exception {
        // 查询资源单价
        Share share = shareRepository.findById(shareId).orElse(null);
        assert share != null;
        Integer price = share.getPrice();

        // 查询用户是否已经兑换过
        MidUserShare midUserShare = midUserShareService.selectRecordWithUserIdAndShareId(userId, shareId);
        // 是否兑换过
        if (midUserShare != null) {
            return share;
        } else {
            // openfeign调用远程服务获取用户信息
            ResponseResult result = userService.getUser(userId, token);
            String jsonStrings = JSONObject.toJSONString(result.getData());
            JSONObject jsonObject = JSONObject.parseObject(jsonStrings);
            User user = JSONObject.toJavaObject(jsonObject, User.class);
            if (user.getBonus() > price) {
                ResponseResult newData = userService.auditUserData(UserProfileAuditDto.builder().id(user.getId()).bonus(user.getBonus() - price).build(), token);
                System.out.println(newData);
                // String newDataStr = JSONObject.toJSONString(newData.getData());
                // JSONObject newJsonObj = JSONObject.parseObject(newDataStr);
                // User newUser = JSONObject.toJavaObject(newJsonObj, User.class);

                // 插入用户兑换表记录
                midUserShareService.insert(MidUserShare.builder().shareId(share.getId()).userId(userId).build());

                // 修改兑换次数
                share.setBuyCount(share.getBuyCount() + 1);
                share = shareRepository.saveAndFlush(share);

                // 插入积分变动记录
                bonusEventLogRepository.saveAndFlush(BonusEventLog.builder()
                        .userId(userId)
                        .value("-" + price)
                        .event("EXCHANGE")
                        .createTime(new Date())
                        .description("兑换资源")
                        .build());

            } else {
                throw new Exception("积分不足");
            }
        }
        return share;
    }


    // @Override
    // public ResponseResult shareList(Integer pageIndex, Integer pageSize, ShareQueryDto shareQueryDto, Integer userId) {
    //     Pageable pageable = PageRequest.of(pageIndex,pageSize);
    //     List<Share> list = new ArrayList<>();
    //     Page<Share> result = shareRepository.findByShowFlagAndTitleLikeAndAuthorLikeAndSummaryLike(
    //             1,
    //             "%"+shareQueryDto.getTitle()+"%",
    //             "%"+shareQueryDto.getName()+"%",
    //             "%"+shareQueryDto.getSummary()+"%",
    //             pageable
    //             );
    //     // 用户没登录
    //     if(userId == 0) {
    //         result.getContent().forEach(share -> {
    //             share.setDownloadUrl("");
    //             list.add(share);
    //         });
    //     }else {
    //         result.getContent().forEach(share -> {
    //             MidUserShare midUserShare = MidUserShare.builder().shareId(share.getId()).userId(userId).build();
    //             Example<MidUserShare> example = Example.of(midUserShare);
    //             if (!midUserShareRepository.findOne(example).isPresent()) {
    //                 share.setDownloadUrl("");
    //             }
    //             list.add(share);
    //         });
    //     }
    //     return ResponseResult.success(list);
    // }

}
