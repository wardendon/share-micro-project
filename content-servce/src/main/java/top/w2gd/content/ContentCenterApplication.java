package top.w2gd.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author w2gd
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"top.w2gd.content"})
public class ContentCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class,args);
    }
}
