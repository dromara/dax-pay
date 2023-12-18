package cn.bootx.platform.daxpay.code;

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
     * 储值卡日志-导入
     */
    String LOG_IMPORT = "import";

    /**
     * 储值卡日志-预冻结额度
     */
    String LOG_FREEZE_BALANCE = "freeze";

    /**
     * 储值卡日志-扣减并解冻余额
     */
    String LOG_REDUCE_AND_UNFREEZE_BALANCE = "reduceAndUnfreeze";

    /**
     * 储值卡日志-直接支付
     */
    String LOG_PAY = "pay";
    /**
     * 储值卡日志-取消支付
     */
    String LOG_CLOSE_PAY = "closePay";

    /**
     * 储值卡日志-退款到本卡中
     */
    String LOG_REFUND_SELF = "refundSelf";

}
