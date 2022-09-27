package top.w2gd.upload.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import top.w2gd.upload.service.UploadService;
import top.w2gd.upload.utils.AliyunResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Description TODO
 * @Date 2022-09-27-21-43
 * @Author qianzhikang
 */

@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    private AliyunResource aliyunResource;
    /**
     * 文件上传
     *
     * @param file 文件对象
     * @return 上传后的url
     */
    @Override
    public String uploadFile(MultipartFile file) {
        String bucketName = aliyunResource.getBucket();
        String endPoint = aliyunResource.getEndpoint();
        String accessKeyId = aliyunResource.getAccessKeyId();
        String accessKeySecret = aliyunResource.getAccessKeySecret();

        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        String fileName = file.getOriginalFilename();
        String[] fileNameArr = fileName.split("\\.");
        String suffix = fileNameArr[fileNameArr.length - 1];
        String uploadFileName = bucketName + UUID.randomUUID().toString() + "." + suffix;

        InputStream inputStream = null;
        try{
            inputStream = file.getInputStream();
        }catch (IOException e){
            System.err.println("文件上传出现异常");
        }
        //上传oss
        ossClient.putObject(bucketName,uploadFileName,inputStream);
        ossClient.shutdown();
        return "https://" + bucketName + "." + endPoint + "/" + uploadFileName;
    }
}
