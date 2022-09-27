package top.w2gd.content.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.w2gd.content.domain.entity.Share;

/**
 * @author w2gd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareDto {
    private Share share;
    private String nickName;
    private String avatar;
}
