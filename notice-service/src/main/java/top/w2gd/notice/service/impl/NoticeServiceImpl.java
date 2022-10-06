package top.w2gd.notice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.w2gd.notice.domain.entity.Notice;
import top.w2gd.notice.respository.NoticeRepository;
import top.w2gd.notice.service.NoticeService;


/**
 * @author w2gd
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor =  @__(@Autowired))
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public Notice getLatestNotice() {
        // 排序规则，根据createTime，降序排列
        Sort sort = Sort.by("createTime").descending();
        return noticeRepository.findByShowFlag(true,sort).get(0);
    }

    /**
     * 获取通知
     *
     * @param pageNum  开始页
     * @param pageSize 每页数量
     * @return 分页数据
     */
    @Override
    public Page<Notice> getNotice(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.by("createTime").descending());
        return noticeRepository.findByShowFlag(true, pageRequest);
    }
}
