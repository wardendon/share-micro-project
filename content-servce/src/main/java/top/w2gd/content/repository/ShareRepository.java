package top.w2gd.content.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import top.w2gd.content.domain.entity.Share;

import java.util.List;

/**
 * @author w2gd
 */
public interface ShareRepository extends JpaRepository<Share, Integer> {

    /**
     * 根据是否显示查询
     * @param showFlag 是否显示
     * @param pageRequest 分页
     * @return 分页Share
     */
    Page<Share> findByShowFlag(Integer showFlag, PageRequest pageRequest);

    /**
     * 根据审核状态显示查询
     * @param auditStatus 审核状态
     * @param pageRequest 分页
     * @return 分页Share
     */
    Page<Share> findByAuditStatus(String auditStatus, PageRequest pageRequest);

    /**
     * 根据用户ID返回数据
     * @param userId 用户ID
     * @return .
     */
    List<Share> findAllByUserId(Integer userId);
}
