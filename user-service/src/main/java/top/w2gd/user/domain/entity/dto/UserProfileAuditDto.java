package top.w2gd.user.domain.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改个人信息传输类
 * @author w2gd
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileAuditDto {

    private Integer id;

    private String mobile;

    private String password;

    private String nickname;

    private String avatar;
}
