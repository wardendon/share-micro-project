package top.w2gd.content.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author w2gd
 */
@Configuration
public class LogConfig {
    @Bean
    Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }

}
