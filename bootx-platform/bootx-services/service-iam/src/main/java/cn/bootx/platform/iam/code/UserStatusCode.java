package cn.bootx.platform.iam.code;

/**
 * 用户状态码
 *
 * @author xxm
 * @since 2021/9/9
 */
public interface UserStatusCode {

    /** 正常 */
    String NORMAL = "normal";

    /** 锁定 多次登录失败 */
    String LOCK = "lock";

    /** 封禁 */
    String BAN = "ban";

}
