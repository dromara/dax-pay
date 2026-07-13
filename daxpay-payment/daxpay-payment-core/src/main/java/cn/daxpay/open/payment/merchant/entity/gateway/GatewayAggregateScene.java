package cn.daxpay.open.payment.merchant.entity.gateway;

import cn.daxpay.open.payment.merchant.enums.CashierSceneEnum;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 网关聚合扫码场景配置(子表)
///
/// 每个聚合配置主表对应多条场景配置(微信/支付宝/云闪付/抖音)。
/// - METHOD 模式: 填 method
/// - DIRECT 模式: 填 channelMchNo + capability
/// - AUTO 模式: 子表可为空
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_gateway_aggregate_scene")
public class GatewayAggregateScene extends MpBaseEntity {

    /// 聚合配置主表ID
    private Long configId;

    /// 场景编码: wechat_pay/alipay/union_pay/douyin
    /// @see CashierSceneEnum
    private String scene;

    /// 支付方式(METHOD 模式填)
    /// @see PayMethodEnum
    private String method;

    /// 通道商户号(DIRECT 模式填, 唯一绑定一个支付产品)
    private String channelMchNo;

    /// 支付能力(DIRECT 模式填)
    /// @see PayCapabilityEnum
    private String capability;
}
