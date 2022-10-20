package top.w2gd.content.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author w2gd
 * @Description TODO
 * @Date 2022-10-06-11-38
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BonusEventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private String value;
    private String event;
    private Date createTime;
    private String description;
}
