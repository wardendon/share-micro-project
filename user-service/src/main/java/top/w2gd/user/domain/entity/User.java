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
 * @Description TODO
 * @Date 2022-09-06-15-49
 * @Author qianzhikang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    /**
     * 主健
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String mobile;


    private String password;


    private String nickname;

    private String roles;

    private String avatar;

    private Date createTime;

    private Date updateTime;

    private Integer bonus;



}
