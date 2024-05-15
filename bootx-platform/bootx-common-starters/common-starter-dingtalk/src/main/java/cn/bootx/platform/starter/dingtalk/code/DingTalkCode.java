package cn.bootx.platform.starter.dingtalk.code;

/**
 * 钉钉消息类型
 *
 * @author xxm
 * @since 2020/11/30
 */
public interface DingTalkCode {

    /** 钉钉Access参数 */
    String ACCESS_TOKEN_PARAM = "?access_token={}";

    /** 内部应用获取access_token */
    String APP_ACCESS_TOKEN_URL = "https://oapi.dingtalk.com/gettoken?appkey={appKey}&appsecret={appSecret}";

    /** 带sign的机器人消息发送请求 */
    String ROBOT_SEND_SIGN_URL = "https://oapi.dingtalk.com/robot/send?access_token={accessToken}&timestamp={timestamp}&sign={sign}";

    /** 不带sign的机器人消息发送请求 */
    String ROBOT_SEND_NOT_SIGN_URL = "https://oapi.dingtalk.com/robot/send" + ACCESS_TOKEN_PARAM;

    /** 发送工作通知 */
    String NOTICE_CORP_SEND_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2"
            + ACCESS_TOKEN_PARAM;

    /** 更新工作通知(OA消息) */
    String NOTICE_CORP_UPDATE_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/status_bar/update"
            + ACCESS_TOKEN_PARAM;

    /** 撤回工作通知 */
    String NOTICE_CORP_RECALL_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/recall"
            + ACCESS_TOKEN_PARAM;

    /** 发送企业群通知 */
    String NOTICE_CHAT_URL = "https://oapi.dingtalk.com/chat/send" + ACCESS_TOKEN_PARAM;

    /** 发送普通消息通知 */
    String NOTICE_MESSAGE_URL = "https://oapi.dingtalk.com/message/send_to_conversation" + ACCESS_TOKEN_PARAM;

    /** 根据unionid获取用户userid */
    String USER_GET_URL = "https://oapi.dingtalk.com/topapi/user/getbyunionid" + ACCESS_TOKEN_PARAM;

    /** 媒体文件上传 */
    String MEDIA_UPLOAD_URL = "https://oapi.dingtalk.com/media/upload" + ACCESS_TOKEN_PARAM;

    /** 钉钉字段 */
    String ACCESS_TOKEN = "accessToken";

    String SIGN = "sign";

    String TIMESTAMP = "timestamp";

    String APP_KEY = "appKey";

    String APP_SECRET = "appSecret";

    /** 应用id */
    String CLIENT_ID = "clientId";

    // 应用密钥
    String CLIENT_SECRET = "clientSecret";

    String CODE = "code";

    // OAuth2.0刷新令牌，从返回结果里面获取。
    String REFRESH_TOKEN = "refreshToken";

    String GRANT_TYPE = "grantType";

    String RESPONSE_TYPE = "responseType";

    String SCOPE = "scope";

    String REDIRECT_URI = "redirectUri";

    String STATE = "state";

    String PROMPT = "prompt";

    /** 媒体 */
    String MEDIA = "media";

    /** 类型 */
    String TYPE = "type";

    /** 请求头参数调用服务端接口的授权凭证 */
    String HEADER_TOKEN = "x-acs-dingtalk-access-token";

    /* 认证字段 */
    /** 授权码换token */
    String AUTH_AUTHORIZATION_CODE = "authorization_code";

    /** 刷新token换用户token */
    String AUTH_REFRESH_TOKEN = "refresh_token";

    /** openid */
    String OPEN_ID = "openid";

    /** union_id */
    String UNION_ID = "unionid";

    /* 响应值 */
    /** 成功 */
    int SUCCESS_CODE = 0;

    /** 未找到对应员工 */
    int NOT_FUND_STAFF = 60121;

    /* 媒体类型 */
    /** 图片 */
    String MEDIA_IMAGE = "image";

    /** 语音 */
    String MEDIA_VOICE = "voice";

    /** 视频 */
    String MEDIA_VIDEO = "video";

    /** 普通文件 */
    String MEDIA_FILE = "file";

}
