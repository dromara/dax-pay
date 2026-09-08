package cn.daxpay.open.payment.common.lock;

import lombok.experimental.UtilityClass;

/// # 交易域分布式锁 key 注册表
///
/// 统一收编交易链路全部 Redis 锁 key 的前缀与工厂方法, 避免 key 前缀字符串在各服务内联拼接
/// (拼写漂移无编译期保护), 并保证同域锁 key 命名口径一致(统一挂 payment: 命名空间)。
///
/// ## 租期两档
/// - 长租期 [LONG_EXPIRE]/[LONG_WAIT]: 发起类操作(锁内含通道 HTTP 调用, 依赖通道超时上限)
/// - 短租期 [SHORT_EXPIRE]/[SHORT_WAIT]: 状态流转/回调幂等类操作(纯本地 DB 写)
@UtilityClass
public class TradeLockKeys {

    /// 长租期: 锁自动过期时间(ms)
    public static final long LONG_EXPIRE = 60_000;

    /// 长租期: 获取锁等待时间(ms)
    public static final long LONG_WAIT = 3_000;

    /// 短租期: 锁自动过期时间(ms)
    public static final long SHORT_EXPIRE = 10_000;

    /// 短租期: 获取锁等待时间(ms)
    public static final long SHORT_WAIT = 50;

    /// 普通支付发起: 按商户号+业务单号(与幂等唯一键 mchNo+bizOrderNo 同口径, 不同商户同单号不互相争锁)
    public String pay(String mchNo, String bizOrderNo) {
        return "payment:pay:" + mchNo + ":" + bizOrderNo;
    }

    /// 网关支付发起: 按网关订单号
    public String gatewayPay(String orderNo) {
        return "payment:gateway:pay:" + orderNo;
    }

    /// 网关预下单: 按商户号+业务单号
    public String gatewayPre(String mchNo, String bizOrderNo) {
        return "payment:gateway:pre:" + mchNo + ":" + bizOrderNo;
    }

    /// 网关超时关单: 按网关订单 id
    public String gatewayTimeout(Long orderId) {
        return "payment:gateway:timeout:" + orderId;
    }

    /// 交易主体(支付/关单/同步/回调/异常单处置共用): 按交易 id
    public String trade(Long tradeId) {
        return "payment:trade:" + tradeId;
    }

    /// 退款交易处置: 按交易号(tradeNo)
    public String refundTrade(String tradeNo) {
        return "payment:refund:trade:" + tradeNo;
    }

    /// 退款回调幂等: 按回调标识
    public String refundCallback(String lockId) {
        return "payment:callback:refund:" + lockId;
    }

    /// 分账发起: 按商户号+业务分账号(与幂等唯一键同口径)
    public String alloc(String mchNo, String bizAllocNo) {
        return "payment:alloc:" + mchNo + ":" + bizAllocNo;
    }

    /// 分账交易处置: 按分账单 id
    public String allocTrade(Long allocOrderId) {
        return "payment:alloc-trade:" + allocOrderId;
    }

    /// 分账发起(按原支付交易号锁, 同一原支付同时最多一笔分账; 与 [allocTrade] 前缀相同但 id 域不同, 勿混用)
    public String allocTradeByPayTradeNo(String tradeNo) {
        return "payment:alloc-trade:" + tradeNo;
    }

    /// 转账发起: 按商户号+业务转账号(与幂等唯一键同口径, 不同商户同单号不互相争锁)
    public String transfer(String mchNo, String bizTransferNo) {
        return "payment:transfer:" + mchNo + ":" + bizTransferNo;
    }

    /// 转账交易处置: 按转账交易 id
    public String transferTrade(Long tradeId) {
        return "payment:transfer-trade:" + tradeId;
    }
}
