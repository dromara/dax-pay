package cn.daxpay.open.payment.merchant.entity.gateway;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.merchant.enums.AggregateConfigLevelEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 码牌支付策略配置(应用级)
///
/// 与 [GatewayAggregateConfig] 拆分; 按 level 控制 (clientEnv × payForm) 解析自由度。
/// - AUTO: 子表可空, 按环境+形态推导默认 method
/// - METHOD: 每环境×形态配置 method
/// - DIRECT: 每环境×形态配置通道商户+能力
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_gateway_code_config")
public class GatewayCodeConfig extends MchBaseEntity {

    /// 应用号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 配置深度: auto/method/direct
    /// @see AggregateConfigLevelEnum
    private String level;
}
