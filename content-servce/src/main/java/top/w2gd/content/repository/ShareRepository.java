package top.w2gd.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.w2gd.content.domain.entity.Share;

/**
 * @author w2gd
 */
public interface ShareRepository extends JpaRepository<Share, Integer> {
}
