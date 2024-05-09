package cn.bootx.platform.notice.code;

/**
 * 短信渠道商配置状态
 * @author xxm
 * @since 2023/8/3
 */
public interface SmsChannelStatusCode {
    /** 正常 */
    String NORMAL = "normal";

    /** 停用 */
    String FORBIDDEN = "forbidden";
}
