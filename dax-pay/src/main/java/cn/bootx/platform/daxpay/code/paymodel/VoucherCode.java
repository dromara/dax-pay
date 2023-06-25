package cn.bootx.platform.daxpay.code.paymodel;

/**
 * 储值卡常量
 *
 * @author xxm
 * @since 2022/3/14
 */
public interface VoucherCode {

    /**
     * 状态-启用
     */
    int STATUS_NORMAL = 1;

    /**
     * 状态-停用
     */
    int STATUS_FORBIDDEN = 2;

    /**
     * 储值卡日志-开通
     */
    int LOG_ACTIVE = 1;

    /**
     * 储值卡日志-支付
     */
    int LOG_PAY = 2;

    /**
     * 储值卡日志-退款
     */
    int LOG_CLOSE = 3;

    /**
     * 储值卡日志-退款
     */
    int LOG_REFUND = 4;

    /**
     * 储值卡日志-Admin余额变动
     */
    int LOG_ADMIN_CHANGER = 4;

}
