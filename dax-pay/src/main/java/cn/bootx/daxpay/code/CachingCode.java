package cn.bootx.daxpay.code;

/**
 * 支付服务缓存
 *
 * @author xxm
 * @date 2022/7/11
 */
public interface CachingCode {

    /** 支付单(主键) */
    String PAYMENT_ID = "pay:payment:id";

    /** 支付单(业务号码) */
    String PAYMENT_BUSINESS_ID = "pay:payment:business";

}
