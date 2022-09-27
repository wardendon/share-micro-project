package top.w2gd.content.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import top.w2gd.content.domain.entity.Notice;

import java.util.List;

/**
 * @author w2gd
 */
public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    /**
     * 根据类型提交查询，根据日期排序
     * @param showFlag 是否显示
     * @param sort 排序条件
     * @return List<Notice>
     */
    List<Notice> findByShowFlag(Boolean showFlag, Sort sort);
}
