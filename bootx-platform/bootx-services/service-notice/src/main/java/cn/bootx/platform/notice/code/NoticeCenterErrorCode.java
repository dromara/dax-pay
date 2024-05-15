package cn.bootx.platform.notice.code;

/**
 * 通知中心错误码
 *
 * @author xxm
 * @since 2021/8/5
 */
public interface NoticeCenterErrorCode {

    /**
     * 邮箱配置编号已存在
     */
    int MAIL_CONFIG_CODE_ALREADY_EXISTED = 24010;

    /**
     * 邮箱配置不存在
     */
    int MAIL_CONFIG_NOT_EXIST = 24011;

    /**
     * 默认邮箱配置已存在
     */
    int DEFAULT_MAIL_CONFIG_ALREADY_EXISTED = 24012;

    /**
     * 短信配置编号已存在
     */
    int SMS_CONFIG_CODE_ALREADY_EXISTED = 24020;

    /**
     * 短信配置不存在
     */
    int SMS_CONFIG_NOT_EXIST = 24021;

    /**
     * 默认短信配置已存在
     */
    int DEFAULT_SMS_CONFIG_ALREADY_EXISTED = 24022;

    /**
     * Bandwidth 短信发送失败
     */
    int BANDWIDTH_SMS_SEND_ERROR = 24023;

    /**
     * WeChat配置编号已存在
     */
    int WECHAT_CONFIG_CODE_ALREADY_EXISTED = 24030;

    /**
     * WeChat配置不存在
     */
    int WECHAT_CONFIG_NOT_EXIST = 24031;

    /**
     * 邮箱配置不存在
     */
    int MAIL_TEMPLATE_NOT_EXIST = 24041;

}
