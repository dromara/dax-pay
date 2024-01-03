package cn.bootx.platform.daxpay.service.code;

/**
 * 钱包涉及到的常量
 *
 * @author xxm
 * @since 2020/12/8
 */
public interface WalletCode {

    /* 操作类型 */
    /** 系统操作 */
    String OPERATION_SOURCE_SYSTEM = "system";

    /** 管理员操作 */
    String OPERATION_SOURCE_ADMIN = "admin";

    /** 用户操作 */
    String OPERATION_SOURCE_USER = "user";

    /* 钱包状态 */
    /** 钱包状态-正常 */
    String STATUS_NORMAL = "normal";

    /** 钱包状态-禁用 */
    String STATUS_FORBIDDEN = "forbidden";

    /* 日志类型 */
    /**
     * 钱包日志-开通
     */
    String LOG_ACTIVE = "active";

    /**
     * 钱包日志-Admin操作余额变动
     */
    String LOG_ADMIN_CHANGER = "adminChanger";

    /**
     * 钱包日志-预冻结额度
     */
    String LOG_FREEZE_BALANCE = "freeze";

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
     * 钱包日志-退款
     */
    String LOG_REFUND = "refund";

    /**
     * 锁定钱包
     */
    String LOG_LOCK = "lock";

    /**
     * 解锁钱包
     */
    String LOG_UNLOCK = "unlock";

}
