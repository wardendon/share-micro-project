package top.w2gd.notice.service;

import top.w2gd.notice.domain.entity.Notice;

/**
 * @author w2gd
 */
public interface NoticeService {

    Notice getLatestNotice();

}
