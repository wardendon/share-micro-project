package top.w2gd.content.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.w2gd.content.domain.dto.AuditShareDto;
import top.w2gd.content.domain.dto.UserAddBounsDto;
import top.w2gd.content.domain.entity.MidUserShare;
import top.w2gd.content.domain.entity.Share;
import top.w2gd.content.domain.enums.ShareAuditEnums;
import top.w2gd.content.repository.ShareRepository;
import top.w2gd.content.service.MidUserShareService;
import top.w2gd.content.service.ShareService;

import java.util.List;
import java.util.Objects;

/**
 * @author w2gd
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor =  @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareRepository shareRepository;

    private final MidUserShareService midUserShareService;

    private final RocketMQTemplate rocketMQTemplate;
    @Override
    public Share findById(Integer id) {
        return shareRepository.findById(id).orElse(null);
    }

    @Override
    public List<Share> finAll() {
        return shareRepository.findAll();
    }
    @SentinelResource(value = "getNumber",blockHandler = "blockHandlerGetNumber")
    @Override
    public String getNumber(int number) {
        return "number = {"+ number +"}";
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
}
