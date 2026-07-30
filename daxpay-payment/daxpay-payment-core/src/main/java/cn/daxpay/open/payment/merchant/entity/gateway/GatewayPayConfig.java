package cn.daxpay.open.payment.merchant.entity.gateway;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 网关支付配置(应用级, 码牌/聚合共用)
///
/// 一个应用一份配置, 码牌支付与聚合扫码共用同一份策略。
/// 按配置深度(level)控制客户端环境解析支付方式的自由度, 明细存储在 [GatewayPayClientEnv] 子表。
/// - AUTO: 子表无需配置, 系统按环境+形态推导支付方式
/// - METHOD: 子表每环境×形态配置支付方式(method)
/// - DIRECT: 子表每环境×形态配置通道商户号(channelMchNo)+支付能力(capability)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_gateway_pay_config")
public class GatewayPayConfig extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 配置深度: auto/method/direct
    /// @see AggregateConfigLevelEnum
    private String level;

    /// 是否自动拉起支付(码牌仅对固定金额生效)
    private Boolean autoLaunch;
}
