package org.dromara.daxpay.payment.pay.order.entity;

import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 合单关系
///
/// 合单支付的聚合容器，记录合单整体信息
/// 关联多个子单 pay_trade（trade_type = combine_sub）
/// 整体状态由子单 pay 聚合计算，乐观锁防并发
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_combine")
public class PayCombineOrder extends MchAppBaseEntity {

    /// 商户合单业务号
    private String bizOrderNo;

    /// 标题
    private String title;

    /// 子单数
    private Integer subCount;

    /// 合单总额（最小货币单位）
    private Long totalAmount;

    /// 币种 ISO 4217，默认 CNY
    private String currency;

    /// 整体状态
    /// @see CombineStatusEnum
    private String status;

    /// 异步通知地址
    private String notifyUrl;

    /// 同步跳转地址
    private String returnUrl;

    /// 商户附加参数
    private String attach;
}
