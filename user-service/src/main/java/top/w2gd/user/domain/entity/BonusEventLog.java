package top.w2gd.user.domain.entity;

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
