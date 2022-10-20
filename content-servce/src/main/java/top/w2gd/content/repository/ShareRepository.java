package top.w2gd.content.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.w2gd.content.domain.dto.ExchangeRecordDto;
import top.w2gd.content.domain.entity.Share;

import java.util.List;

/**
 * @author w2gd
 */
public interface ShareRepository extends JpaRepository<Share, Integer> , JpaSpecificationExecutor<Share> {

    /**
     * 根据是否显示查询
     *
     * @param showFlag    是否显示
     * @param pageRequest 分页
     * @return 分页Share
     */
    Page<Share> findByShowFlag(Integer showFlag, PageRequest pageRequest);

    /**
     * 根据审核状态显示查询
     *
     * @param auditStatus 审核状态
     * @param pageRequest 分页
     * @return 分页Share
     */
    Page<Share> findByAuditStatus(String auditStatus, PageRequest pageRequest);

    /**
     * 根据用户ID返回数据
     *
     * @param userId     用户ID
     * @param createTime 创建时间
     * @return .
     */
    List<Share> findAllByUserId(Integer userId, Sort createTime);


    // Page<Share> findByShowFlagAndTitleLikeAndAuthorLikeAndSummaryLike( Integer showFlag, String title, String author, String summary, Pageable pageable);

    @Query(value = "SELECT new top.w2gd.content.domain.dto.ExchangeRecordDto(s.title,s.cover,s.createTime) FROM Share s WHERE s.id IN (SELECT m.shareId FROM MidUserShare m WHERE m.userId = ?1) AND s.userId <> ?1")
    Page<ExchangeRecordDto> findExchange(Integer userId, Pageable pageable);
}
