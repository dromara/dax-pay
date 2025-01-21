package cn.bootx.platform.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 用户信息类
 *
 * @author xxm
 * @since 2021/7/30
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class UserDetail {

    /** 用户id */
    private Long id;

    /** 用户名称 */
    private String name;

    /** 账号 */
    private String account;

    /** 不进行持久化 */
    @JsonIgnore
    private transient String password;

    /** 是否管理员 */
    private boolean admin;

    /**
     * 账号状态
     * @see cn.bootx.platform.iam.code.UserStatusCode
     */
    private String status;

    /**
     * 扩展信息
     */
    private Map<String,String> ext;

}
