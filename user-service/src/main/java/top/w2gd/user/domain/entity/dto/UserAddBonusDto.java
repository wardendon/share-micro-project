package top.w2gd.user.domain.entity.dto;

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
public class UserAddBonusDto {
    private Integer userId;
    private Integer bonus;
}
