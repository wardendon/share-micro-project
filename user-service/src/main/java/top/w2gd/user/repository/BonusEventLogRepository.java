package top.w2gd.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import top.w2gd.user.domain.entity.BonusEventLog;

/**
 * @author w2gd
 */
public interface BonusEventLogRepository extends JpaRepository<BonusEventLog,Integer> {

    /**
     * 查询积分明细
     * @param userId 用户id
     * @param pageable 分页
     * @return 分页积分明细
     */
    Page<BonusEventLog> findByUserId(Integer userId, Pageable pageable);
}
