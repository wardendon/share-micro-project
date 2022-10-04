package top.w2gd.content.admincontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.w2gd.content.auth.CheckAuthorization;
import top.w2gd.content.auth.CheckLogin;
import top.w2gd.content.common.ResponseResult;
import top.w2gd.content.domain.dto.AuditShareDto;
import top.w2gd.content.domain.entity.Share;
import top.w2gd.content.service.ShareService;

/**
 * @author w2gd
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private final ShareService shareService;
    @PostMapping("/verify")
    @CheckLogin
    @CheckAuthorization("admin")
    public ResponseResult verifyContent(@RequestBody AuditShareDto auditShareDto){
        Share share = shareService.auditShare(auditShareDto);

        return ResponseResult.success(share);
    }
}
