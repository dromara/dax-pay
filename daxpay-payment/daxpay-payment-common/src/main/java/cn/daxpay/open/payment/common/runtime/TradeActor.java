package cn.daxpay.open.payment.common.runtime;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 交易主体身份(线程级)
///
/// 标识"当前操作代表哪个商户",贯穿整个请求生命周期,
/// 供 MyBatis 租户隔离与商户字段自动填充隐式读取。
///
/// appId 不在此处:它是可空、可推导的应用归属,属于业务数据,
/// 由调用方显式赋值到交易/订单实体,不进线程上下文。
@Data
@Accessors(chain = true)
public class TradeActor {

    /// 商户号
    private String mchNo;
}
