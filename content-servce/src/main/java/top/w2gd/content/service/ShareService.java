package top.w2gd.content.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import top.w2gd.content.domain.entity.Share;

import java.util.List;

/**
 * @author w2gd
 */
public interface ShareService {
    /**
     * 111
     * @param id 1
     * @return 1
     */
    Share findById(Integer id);

    List<Share> finAll();

    String getNumber(int number);

    String blockHandlerGetNumber(int number, BlockException e);
}
