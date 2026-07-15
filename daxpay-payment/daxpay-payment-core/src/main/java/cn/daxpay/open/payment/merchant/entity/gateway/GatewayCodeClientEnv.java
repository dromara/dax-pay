package cn.daxpay.open.payment.merchant.entity.gateway;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 码牌支付策略客户端环境配置(子表)
///
/// 唯一维度: configId + clientEnv + payForm(h5/mini)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_gateway_code_client_env")
public class GatewayCodeClientEnv extends MchBaseEntity {

    /// 码牌支付策略主表 ID
    private Long configId;

    /// 客户端环境编码
    /// @see ClientEnvEnum
    private String clientEnv;

    /// 支付形态 h5/mini
    /// @see CodePayFormEnum
    private String payForm;

    /// 支付方式(METHOD 模式)
    /// @see PayMethodEnum
    private String method;

    /// 通道商户号(DIRECT 模式)
    private String channelMchNo;

    /// 支付能力(DIRECT 模式)
    /// @see PayCapabilityEnum
    private String capability;
}
