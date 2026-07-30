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

/// # 网关支付客户端环境配置(子表, 码牌/聚合共用)
///
/// 唯一维度: configId + clientEnv + payForm(h5/mini)
/// - METHOD 模式: 填 method
/// - DIRECT 模式: 填 channelMchNo + capability
/// - AUTO 模式: 子表可为空
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pay_gateway_pay_client_env")
public class GatewayPayClientEnv extends MchBaseEntity {

    /// 网关支付配置主表 ID
    private Long configId;

    /// 客户端环境编码: wechat/alipay/union_pay/douyin
    /// @see ClientEnvEnum
    private String clientEnv;

    /// 支付形态 h5/mini
    /// @see CodePayFormEnum
    private String payForm;

    /// 支付方式(METHOD 模式填)
    /// @see PayMethodEnum
    private String method;

    /// 通道商户号(DIRECT 模式填, 唯一绑定一个支付产品)
    private String channelMchNo;

    /// 支付能力(DIRECT 模式填)
    /// @see PayCapabilityEnum
    private String capability;
}
