package top.w2gd.notice.service;

import org.springframework.data.domain.Page;
import top.w2gd.notice.domain.entity.Notice;

/**
 * @author w2gd
 */
public interface NoticeService {

    Notice getLatestNotice();

    /**
     * 获取通知
     * @param pageNum 开始页
     * @param pageSize 每页数量
     * @return 分页数据
     */
    Page<Notice> getNotice(int pageNum, int pageSize);

}
