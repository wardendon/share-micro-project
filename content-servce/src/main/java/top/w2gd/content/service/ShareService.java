package top.w2gd.content.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.data.domain.Page;
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

    // List<Share> findShares(Boolean showFlag,AudiStatusEnum audiStatus);

    String getNumber(int number);

    /**
     * 审核分享内容
     * @param auditShareDto 分享内容dto
     * @return 分享内容详情
     */
    Share auditShare(AuditShareDto auditShareDto);

    String blockHandlerGetNumber(int number, BlockException e);

    /**
     * 获取分页资源
     * @param pageNum 当前页
     * @param pageSize 每页数量
     * @return 分页数据
     */
    Page<Share> getPageShare(int pageNum, int pageSize);

    /**
     * 根据审核状态显示查询
     * @param pageNum 当前页
     * @param pageSize 每页数量
     * @param status 审核状态
     * @return 分页数据
     */
    Page<Share> getPageShareByAudit(int pageNum, int pageSize,String status);

    /**
     * 根据用户ID返回shares
     * @param userId 用户ID
     * @return sharesList
     */
    List<Share> getSharesByUserId(Integer userId);

}
