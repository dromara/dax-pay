package cn.bootx.platform.daxpay.code.paymodel;

/**
 * 储值卡常量
 *
 * @author xxm
 * @since 2022/3/14
 */
public interface VoucherCode {

    /**
     * 状态-正常
     */
    String STATUS_NORMAL = "normal";

    /**
     * 状态-停用
     */
    String STATUS_FORBIDDEN = "forbidden";

    /**
     * 储值卡日志-开通
     */
    String LOG_ACTIVE = "active";

    /**
     * 钱包日志-预冻结额度
     */
    String LOG_FREEZE_BALANCE = "freeze";

    /**
     * 钱包日志-解冻额度
     */
    String LOG_UNFREEZE_BALANCE = "unfreeze";

    /**
     * 钱包日志-解冻并扣减余额
     */
    String LOG_REDUCE_AND_UNFREEZE_BALANCE = "reduceAndUnfreeze";

    /**
     * 钱包日志-直接支付
     */
    String LOG_PAY = "pay";
    /**
     * 钱包日志-取消支付
     */
    String LOG_CLOSE_PAY = "closePay";

    /**
     * 钱包日志-退款到本卡中
     */
    String LOG_REFUND_SELF = "refundSelf";
    /**
     * 钱包日志-统一退款到指定卡中
     */
    String LOG_REFUND_ONE = "refundOne";

    /**
     * 储值卡日志-Admin余额变动
     */
    String LOG_ADMIN_CHANGER = "changer";

}
