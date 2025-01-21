package cn.bootx.platform.core.code;

/**
 * web请求头常量
 *
 * @author network
 */
public interface WebHeaderCode {

    /** ip */
    String X_FORWARDED_FOR = "X-Forwarded-For";

    /** 用户代理 */
    String USER_AGENT = "User-Agent";

    /** 请求头中 operatorId 的参数名 */
    String OPERATOR_ID = "OperatorId";

    /** 请求头中 ACCESS_TOKEN 的参数名 */
    String ACCESS_TOKEN = "AccessToken";

    /** 请求头中 Jwt_Token 的参数名 */
    String JWT_TOKEN = "JwtToken";

    /** 请求头中 Bearer 的参数名 */
    String BEARER = "Bearer";

    /** 请求头中 Authorization 的参数名 */
    String AUTH = "Authorization";

    /** 幂等请求token */
    String IDEMPOTENT_TOKEN = "IdempotentToken";

    /** 请求头 tid 的参数名 */
    String TID = "Tid";

    /** 请求头 channelId 的参数名 */
    String CHANNEL_ID = "ChannelId";

    /** 请求头中 appId 的参数名 */
    String APP_ID = "AppId";

    /** 请求头中 appName 的参数名 */
    String APP_NAME = "AppName";

    /** 请求头中 timezone 的参数名 */
    String TIMEZONE_NAME = "Timezone";

}
