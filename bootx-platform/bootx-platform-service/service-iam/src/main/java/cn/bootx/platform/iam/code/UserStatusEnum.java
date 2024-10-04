package cn.bootx.platform.iam.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态码
 *
 * @author xxm
 * @since 2021/9/9
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    /** 正常 */
    NORMAL("normal", "正常"),

    /** 锁定 多次登录失败 */
    LOCK("lock", "锁定"),

    /** 封禁 */
    BAN ("ban", "封禁");

    private final String code;
    private final String name;

}
