package cn.bootx.platform.notice.code;

/**
 * 邮箱常量
 *
 * @author xxm
 * @since 2021/8/5
 */
public interface MailCode {

    /**
     * 普通方式
     */
    Integer SECURITY_TYPE_PLAIN = 1;

    /**
     * TLS方式
     */
    Integer SECURITY_TYPE_TLS = 2;

    /**
     * SSL方式
     */
    Integer SECURITY_TYPE_SSL = 3;

}
