package cn.bootx.platform.starter.wechat.code;

/**
 * 微信编码
 *
 * @author xxm
 * @since 2022/8/5
 */
public interface WeChatCode {

    /** qrscene_为前缀，后面为二维码的参数值 */
    String EVENT_KEY_QRSCENE = "qrscene_";

    /* 带参数的扫码事件(qrscene_为前缀)参数前缀, 需要配合 qrscene_ 一起使用 */
    /** 扫码登录 */
    String QRSCENE_LOGIN = "login_";

    /* 扫码登录状态 */
    /** 已过期 */
    String QR_LOGIN_EXPIRED = "expired";

    /** 等待中 */
    String QR_LOGIN_WAIT = "wait";

    /**  */
    String QR_LOGIN_OK = "ok";

}
