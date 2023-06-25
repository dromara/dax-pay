package cn.bootx.platform.daxpay.code;

/**
 * 商户和应用相关编码
 *
 * @author xxm
 * @since 2023/6/12
 */
public interface MchAndAppCode {

    /* 商户状态 */
    /** 正常 */
    String MCH_STATE_NORMAL = "normal";

    /** 停用 */
    String MCH_STATE_FORBIDDEN = "forbidden";

    /** 封禁 */
    String MCH_STATE_BANNED = "banned";

    /* 商户应用状态 */
    /** 正常 */
    String MCH_APP_STATE_NORMAL = "normal";

    /** 停用 */
    String MCH_APP_STATE_FORBIDDEN = "forbidden";

    /** 封禁 */
    String MCH_APP_STATE_BANNED = "banned";

    /* 应用关联支付配置状态 */
    /** 正常 */
    String PAY_CONFIG_STATE_NORMAL = "normal";

    /** 停用 */
    String PAY_CONFIG_STATE_FORBIDDEN = "forbidden";

    /** 封禁 */
    String PAY_CONFIG_STATE_BANNED = "banned";

}
