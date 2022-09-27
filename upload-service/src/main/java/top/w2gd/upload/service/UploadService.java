package top.w2gd.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 文件上传服务
 * @Date 2022-09-27-21-42
 * @Author qianzhikang
 */
public interface UploadService {
    /**
     * 文件上传
     * @param file 文件对象
     * @return 上传后的url
     */
    String uploadFile(MultipartFile file);
}
