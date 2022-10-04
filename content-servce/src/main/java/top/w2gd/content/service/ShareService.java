package top.w2gd.content.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import top.w2gd.content.domain.dto.AuditShareDto;
import top.w2gd.content.domain.entity.Share;

import java.util.List;

/**
 * @author w2gd
 */
public interface ShareService {
    /**
     * 111
     * @param id 1
     * @return 1
     */
    Share findById(Integer id);

    List<Share> finAll();

    String getNumber(int number);

    /**
     * 审核分享内容
     * @param auditShareDto 分享内容dto
     * @return 分享内容详情
     */
    Share auditShare(AuditShareDto auditShareDto);

    String blockHandlerGetNumber(int number, BlockException e);

}
