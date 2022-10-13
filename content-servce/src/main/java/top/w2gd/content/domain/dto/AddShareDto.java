package top.w2gd.content.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author w2gd
 * 新增share DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddShareDto {
    private Integer userId;
    private String title;
    private Integer isOriginal;
    private String author;
    private String cover;
    private String summary;
    private Integer price;
    private String downloadUrl;
}
