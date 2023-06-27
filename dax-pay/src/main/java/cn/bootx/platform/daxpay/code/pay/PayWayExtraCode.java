package cn.bootx.platform.daxpay.code.pay;

/**
 * 支付方式扩展字段
 *
 * @author xxm
 * @since 2022/2/27
 */
public interface PayWayExtraCode {

    /** 付款码 */
    String AUTH_CODE = "auth_code";

    /** openId */
    String OPEN_ID = "open_id";

    /** 单张储值卡 */
    String VOUCHER_NO = "voucher_no";

    /** 钱包ID */
    String WALLET_ID = "wallet_id";

    /** 用户ID */
    String USER_ID = "user_id";

    /** 同步通知路径 支付完成跳转的页面地址 */
    String RETURN_URL = "return_url";

}
