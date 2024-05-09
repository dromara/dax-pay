package cn.bootx.platform.starter.auth.code;

/**
 * 认证录方式
 *
 * @author xxm
 * @since 2021/8/2
 */
public interface AuthLoginTypeCode {

    // 参数
    /** 第三方平台授权码 */
    String AUTH_CODE = "authCode";

    /**
     * 访问AuthorizeUrl后回调时带的参数state，用于和请求AuthorizeUrl前的state比较，防止CSRF攻击
     */
    String STATE = "state";

    /** 成功代码 */
    int SUCCESS = 2000;

    /* 登录方式 */
    /** 账号密码登录(普通) */
    String PASSWORD = "password";

    /** 账号密码登录(GoView) */
    String PASSWORD_GO_VIEW = "passwordGoView";

    /** 手机号登录 */
    String PHONE = "phone";

    /** 微信登录(公众号) */
    String WE_CHAT = "weChat";


    /** 微信登录(小程序) */
    String WE_CHAT_APPLET = "weChatApplet";

    /** 微信登录(开放平台) */
    String WE_CHAT_OPEN = "weChatOpen";

    /** 企业微信 */
    String WE_COM = "weCom";

    /** qq登录 */
    String QQ = "qq";

    /** 码云 */
    String GITEE = "gitee";

    /** 飞书 */
    String FEISHU = "feishu";

    /** 钉钉登录 */
    String DING_TALK = "dingTalk";

}
