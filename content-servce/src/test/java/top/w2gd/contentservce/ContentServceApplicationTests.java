package top.w2gd.contentservce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import top.w2gd.content.domain.entity.Share;
import top.w2gd.content.repository.MidUserShareRepository;
import top.w2gd.content.repository.ShareRepository;
import top.w2gd.content.service.MidUserShareService;

import javax.annotation.Resource;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ContentServceApplicationTests {

    @Resource
    MidUserShareRepository midUserShareRepository;
    // aShareRepository shareRepository;
    @Test
    void contextLoads() {
        System.out.println(midUserShareRepository.findByUserIdAndAndShareId(1,1));
    }

}
