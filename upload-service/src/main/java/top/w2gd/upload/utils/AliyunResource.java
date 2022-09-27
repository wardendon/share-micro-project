package top.w2gd.upload.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description 阿里云oss配置
 * @Date 2022-03-23-22-26
 * @Author Courage
 */

@Component
@Data
@PropertySource(value = "classpath:aliyun.properties",encoding = "utf-8")
@ConfigurationProperties(prefix = "aliyun")
public class AliyunResource {
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
    private String endpoint;
}
