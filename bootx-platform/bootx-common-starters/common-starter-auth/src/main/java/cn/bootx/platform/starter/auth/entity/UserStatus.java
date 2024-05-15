package cn.bootx.platform.starter.auth.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;

/**
 * 用户状态上下文
 * @author xxm
 * @since 2023/11/25
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserStatus extends HashMap<String,Object> {

    /** 是否初始密码 */
    private boolean initialPassword;

    /** 密码是否过期 */
    private boolean expirePassword;


}
