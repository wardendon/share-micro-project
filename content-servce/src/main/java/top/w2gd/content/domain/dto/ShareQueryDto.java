package top.w2gd.content.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  wd
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShareQueryDto {
    String title;
    String summary;
    String author;
}
