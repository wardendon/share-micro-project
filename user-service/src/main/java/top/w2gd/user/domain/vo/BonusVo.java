package top.w2gd.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author w2gd
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BonusVo {
    private Integer id;
    private Integer bonus;
}
