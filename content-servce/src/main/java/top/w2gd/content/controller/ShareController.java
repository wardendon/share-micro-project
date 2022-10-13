package top.w2gd.content.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import top.w2gd.content.auth.CheckAuthorization;
import top.w2gd.content.auth.CheckLogin;
import top.w2gd.content.common.ResponseResult;
import top.w2gd.content.common.ResultCode;
import top.w2gd.content.domain.dto.AddShareDto;
import top.w2gd.content.domain.dto.AuditShareDto;
import top.w2gd.content.domain.dto.ShareDto;
import top.w2gd.content.domain.entity.Share;
import top.w2gd.content.domain.entity.User;
import top.w2gd.content.openfeign.UserService;
import top.w2gd.content.service.ShareService;

import java.util.Date;

/**
 * @author w2gd
 */
@RestController
@Slf4j
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {
    private final ShareService shareService;
    private final UserService userService;


    @GetMapping("/all")
    @SentinelResource(value = "getAllShares")
    public ResponseResult getAllShares(){
        String result = shareService.getNumber(2022);
        log.info(result);
        if ("BLOCKED".equals(result)) {
            return ResponseResult.failure(ResultCode.INTERFACE_EXCEED_LOAD);
        }
        return ResponseResult.success(shareService.finAll());
    }

    @GetMapping("{id}")
    @SentinelResource(value = "getSharesById")
    public ResponseResult getShareById(@PathVariable Integer id) {
        String result = shareService.getNumber(2025);
        log.info(result);
        if ("BLOCKED".equals(result)) {
            return ResponseResult.failure(ResultCode.INTERFACE_EXCEED_LOAD);
        }
        Share share = shareService.findById(id);
        Integer userId = share.getUserId();
        ResponseResult res = userService.getUser(userId);
        String jsonString = JSONObject.toJSONString(res.getData());
        JSONObject obj = JSONObject.parseObject(jsonString);
        User user = JSONObject.toJavaObject(obj,User.class);

        System.out.println(user);

        ShareDto shareDto = ShareDto.builder().share(share).nickName(user.getNickname()).avatar(user.getAvatar()).build();

        return ResponseResult.success(shareDto);
    }

    public ResponseResult getAllSharesFallback(){
        log.info("接口被熔断降级");
        return ResponseResult.failure(ResultCode.INTERFACE_FALLBACK);
    }

    @GetMapping("/page-shares")
    public ResponseResult getShares(@RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseResult.success(shareService.getPageShare(pageNum, pageSize));
    }

    /**
     * 根据审核状态返回shares列表
     * @param pageNum 页
     * @param pageSize 数
     * @param status 状态
     * @return sharesList
     */
    @GetMapping("/audit-status")
    public ResponseResult getSharesByAuditStatus(@RequestParam int pageNum,@RequestParam int pageSize, @RequestParam String status) {
        return ResponseResult.success(shareService.getPageShareByAudit(pageNum,pageSize,status));
    }

    /**
     * 根据用户id 返回shares
     * @param userId yhu id
     * @return .
     */
    @GetMapping("/my-shares")
    public ResponseResult getSharesByUserId(@RequestParam int userId) {
        return ResponseResult.success(shareService.getSharesByUserId(userId));
    }
    @PostMapping("/add")
    @CheckLogin
    public ResponseResult addShares(@RequestBody AddShareDto asd){
        Date date = new Date();
        Share share = Share.builder()
                .userId(asd.getUserId())
                .title(asd.getTitle())
                .createTime(date)
                .updateTime(date)
                .isOriginal(asd.getIsOriginal())
                .author(asd.getAuthor())
                .cover(asd.getCover())
                .summary(asd.getSummary())
                .price(asd.getPrice())
                .downloadUrl(asd.getDownloadUrl())
                .buyCount(0)
                .showFlag(0)
                .auditStatus("NOT_YET")
                .reason("")
                .build();
        return ResponseResult.success(shareService.addShare(share));
    }
}
