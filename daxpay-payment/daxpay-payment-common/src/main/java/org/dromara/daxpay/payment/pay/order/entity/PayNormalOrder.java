package org.dromara.daxpay.payment.pay.order.entity;

import org.dromara.daxpay.payment.common.entity.merchant.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 普通支付业务单容器
///
/// 普通支付场景的容器，承载商户业务单信息（bizOrderNo / 商品标题 / 回调地址 等）
/// 与 pay_trade 一对一关联（trade_type = normal）
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_normal_order")
public class PayNormalOrder extends MchAppBaseEntity {

    /// 商户业务单号
    private String bizOrderNo;

    /// 标题
    private String title;

    /// 描述
    private String description;

    /// 业务状态
    /// @see NormalOrderStatusEnum
    private String status;

    /// 异步通知地址（出站商户通知用）
    private String notifyUrl;

    /// 同步跳转地址
    private String returnUrl;

    /// 商户附加参数（回调原样返回）
    private String attach;

    /// 业务单过期时间
    private OffsetDateTime expiredTime;
}
