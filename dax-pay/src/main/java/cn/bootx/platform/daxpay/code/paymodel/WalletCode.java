package cn.bootx.platform.daxpay.code.paymodel;

/**
 * 钱包涉及到的常量
 *
 * @author xxm
 * @date 2020/12/8
 */
public interface WalletCode {

    /* 操作类型 */
    /** 系统操作 */
    String OPERATION_SOURCE_SYSTEM = "1";

    /** 管理员操作 */
    String OPERATION_SOURCE_ADMIN = "";

    /** 用户操作 */
    String OPERATION_SOURCE_USER = "3";

    /* 钱包状态 */
    /** 钱包状态-正常 */
    String STATUS_NORMAL = "";

    /** 钱包状态-禁用 */
    String STATUS_FORBIDDEN = "";

    /* 日志类型 */
    /**
     * 钱包日志-开通
     */
    String LOG_ACTIVE = "";

    /**
     * 钱包日志-主动充值
     */
    String LOG_RECHARGE = "";

    /**
     * 钱包日志-自动充值
     */
    String LOG_AUTO_RECHARGE = "";

    /**
     * 钱包日志-Admin余额变动
     */
    String LOG_ADMIN_CHANGER = "";

    /**
     * 钱包日志-支付
     */
    String LOG_PAY = "";

    /**
     * 钱包日志-系统扣除余额的日志
     */
    String LOG_SYSTEM_REDUCE_BALANCE = "";

    /**
     * 钱包日志-退款
     */
    String LOG_REFUND = "";

    /**
     * 钱包日志-取消支付返还
     */
    String LOG_PAY_CLOSE = "";

}
