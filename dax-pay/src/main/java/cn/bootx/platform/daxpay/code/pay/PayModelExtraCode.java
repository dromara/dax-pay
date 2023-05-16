package cn.bootx.platform.daxpay.code.pay;

/**
 * 支付方式扩展字段
 *
 * @author xxm
 * @date 2022/2/27
 */
public interface PayModelExtraCode {

    /** 付款码 */
    String AUTH_CODE = "authCode";

    /** openId */
    String OPEN_ID = "openId";

    /** 单张储值卡 */
    String VOUCHER_NO = "voucherNo";

    /** 同步通知路径 支付完成跳转的页面地址 */
    String RETURN_URL = "returnUrl";

}
