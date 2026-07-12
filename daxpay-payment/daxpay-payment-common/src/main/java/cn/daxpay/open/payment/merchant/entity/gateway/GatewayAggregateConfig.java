package cn.daxpay.open.payment.merchant.entity.gateway;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.common.enums.AggregateConfigLevelEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 网关聚合扫码配置(应用级)
///
/// 按配置深度(level)控制场景解析支付方式的自由度, 场景明细存储在 [GatewayAggregateScene] 子表。
/// - AUTO: 子表无需配置, 系统按扫码环境推导支付方式
/// - METHOD: 子表每场景配置支付方式(method)
/// - DIRECT: 子表每场景配置通道商户号(channelMchNo)+支付能力(capability)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_gateway_aggregate_config")
public class GatewayAggregateConfig extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 配置深度: auto/method/direct
    /// @see AggregateConfigLevelEnum
    private String level;

    /// 是否自动拉起支付
    private Boolean autoLaunch;
}
