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
import top.w2gd.content.domain.dto.*;
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
     * ??????????????????
     * ??????????????????
     * @return List
     */
    @Override
    public Page<Share> getAll(int pageNum, int pageSize, ShareQueryDto shareQueryDto, Integer userId) {
        // ????????????
        Pageable pageable = PageRequest.of(pageNum, pageSize,Sort.by(Sort.Direction.DESC,"createTime"));
        // ????????????
        Page<Share> all = shareRepository.findAll(new Specification<Share>() {
            @Override
            public Predicate toPredicate(Root<Share> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                // ??????????????????
                predicates.add(criteriaBuilder.equal(root.get("showFlag").as(Integer.class), 1));
                // ????????????????????????????????????
                if (shareQueryDto != null) {
                    // ????????????
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
     * ??????????????????
     *
     * @param auditShareDto ????????????dto
     * @return ??????????????????
     */
    @Override
    public Share auditShare(AuditShareDto auditShareDto) {
        Share share = shareRepository.findById(auditShareDto.getId()).orElse(null);
        assert share != null;
        // if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
        //     throw new IllegalArgumentException("????????????????????????????????????");
        // }
        share.setAuditStatus(auditShareDto.getShareAuditEnums().toString());
        share.setReason(auditShareDto.getReason());
        share.setShowFlag(auditShareDto.getShowFlag() ? 1 : 0);

        Share newShare = shareRepository.saveAndFlush(share);
        // ????????????????????????
        midUserShareService.insert(
                MidUserShare.builder()
                        .shareId(newShare.getId())
                        .userId(newShare.getUserId())
                        .build()

        );

        // ?????????PASS,?????????????????????rocketmq,??????????????????????????????????????????????????????
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
     * ??????????????????
     *
     * @param pageNum  ?????????
     * @param pageSize ????????????
     * @return ????????????
     */
    @Override
    public Page<Share> getPageShare(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.by("createTime").descending());
        return shareRepository.findByShowFlag(1,pageRequest);
    }

    /**
     *
     * @param pageNum ?????????
     * @param pageSize ????????????
     * @param status ????????????
     * @return ????????????
     */
    @Override
    public Page<Share> getPageShareByAudit(int pageNum, int pageSize, String status) {
        PageRequest pageRequest = PageRequest.of(pageNum,pageSize,Sort.by("createTime").descending());
        return shareRepository.findByAuditStatus(status,pageRequest);
    }

    /**
     * ????????????ID ??????shares
     * @param userId ??????ID
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
     * ????????????
     *
     * @param shareId ??????id
     * @param userId  ??????id
     * @return ???????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Share exchange(Integer shareId, Integer userId, String token) throws Exception {
        // ??????????????????
        Share share = shareRepository.findById(shareId).orElse(null);
        assert share != null;
        Integer price = share.getPrice();

        // ?????????????????????????????????
        MidUserShare midUserShare = midUserShareService.selectRecordWithUserIdAndShareId(userId, shareId);
        // ???????????????
        if (midUserShare != null) {
            return share;
        } else {
            // openfeign????????????????????????????????????
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

                // ???????????????????????????
                midUserShareService.insert(MidUserShare.builder().shareId(share.getId()).userId(userId).build());

                // ??????????????????
                share.setBuyCount(share.getBuyCount() + 1);
                share = shareRepository.saveAndFlush(share);

                // ????????????????????????
                bonusEventLogRepository.saveAndFlush(BonusEventLog.builder()
                        .userId(userId)
                        .value("-" + price)
                        .event("EXCHANGE")
                        .createTime(new Date())
                        .description("????????????")
                        .build());

            } else {
                throw new Exception("????????????");
            }
        }
        return share;
    }

    /**
     * ??????????????????
     *
     * @param pageNum  ??????
     * @param pageSize ??????
     * @param userId   ??????id
     * @return ??????????????????
     */
    @Override
    public Page<ExchangeRecordDto> getExchangeRecord(Integer pageNum, Integer pageSize, Integer userId) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createTime").descending());
        return shareRepository.findExchange(userId,pageable);
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
    //     // ???????????????
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
