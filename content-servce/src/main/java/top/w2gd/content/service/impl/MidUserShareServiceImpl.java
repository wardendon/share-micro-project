package top.w2gd.content.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.w2gd.content.domain.entity.MidUserShare;
import top.w2gd.content.repository.MidUserShareRepository;
import top.w2gd.content.service.MidUserShareService;

/**
 * @author w2gd
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MidUserShareServiceImpl implements MidUserShareService {
    private final MidUserShareRepository midUserShareRepository;

    @Override
    public void insert(MidUserShare midUserShare) {
        midUserShareRepository.save(midUserShare);
    }

    /**
     * 根据userID 和 shareID 查询记录
     *
     * @param userId  用户id
     * @param shareId shareId
     * @return 记录
     */
    @Override
    public MidUserShare selectRecordWithUserIdAndShareId(int userId, int shareId) {
        return midUserShareRepository.findByUserIdAndAndShareId(userId,shareId);
    }
}
