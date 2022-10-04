package top.w2gd.content.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.w2gd.content.domain.dto.AuditShareDto;
import top.w2gd.content.domain.entity.Share;
import top.w2gd.content.repository.ShareRepository;
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
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法！该分享已审核！");
        }
        share.setAuditStatus(auditShareDto.getShareAuditEnums().toString());
        share.setReason(auditShareDto.getReason());
        share.setShowFlag(auditShareDto.getShowFlag() ? 1 : 0);
        shareRepository.saveAndFlush(share);
        return share;
    }


    @Override
    public String blockHandlerGetNumber(int number, BlockException e) {
        return "BLOCKED";
    }
}
