package cn.bootx.platform.daxpay.code.pay;

/**
 * 支付方式 1-10 通用支付方式 10 以上是各支付通道特色支付方式, 规则 {通道代码}+{特色支付方式代码}, 如微信小程序支付, 23 微信代码2, 小程序3
 *
 * @author xxm
 * @date 2021/7/26
 */
public interface PayWayCode {

    /**
     * 常规支付. 钱包/积分等
     */
    int NORMAL = 0;

    /** wap支付 */
    int WAP = 1;

    /** 应用支付 */
    int APP = 2;

    /** web支付 */
    int WEB = 3;

    /** 二维码扫码支付 */
    int QRCODE = 4;

    /** 付款码支付 */
    int BARCODE = 5;

    /**
     * 微信公众号支付或者小程序支付
     */
    int JSAPI = 23;

}
