package top.w2gd.notice.respository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import top.w2gd.notice.domain.entity.Notice;

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

    /**
     * 分页根据是否显示提交查询，指定排序规则
     * @param showFlag 是否显示
     * @param pageRequest  分页规则
     * @return 分页数据
     */
    Page<Notice> findByShowFlag(Boolean showFlag, PageRequest pageRequest);
}
