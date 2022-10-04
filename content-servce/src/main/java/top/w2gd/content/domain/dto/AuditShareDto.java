package top.w2gd.content.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.w2gd.content.domain.enums.ShareAuditEnums;

/**
 * 审核dto
 *  2022-10-04-17-57
 * @author wd
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditShareDto {
    private Integer id;
    private ShareAuditEnums shareAuditEnums;
    private String reason;
    private Boolean showFlag;
}