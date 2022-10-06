package top.w2gd.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.w2gd.user.domain.entity.BonusEventLog;

/**
 * @author w2gd
 */
public interface BonusEventLogRepository extends JpaRepository<BonusEventLog,Integer> {
}
