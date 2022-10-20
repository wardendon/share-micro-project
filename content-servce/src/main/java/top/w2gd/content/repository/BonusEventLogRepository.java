package top.w2gd.content.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import top.w2gd.content.domain.entity.BonusEventLog;

/**
 * @Description TODO
 * @Date 2022-10-06-11-37
 * @Author qianzhikang
 */
public interface BonusEventLogRepository extends JpaRepository<BonusEventLog,Integer> {
}
