package cn.daxpay.open.payment.core.assist;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 认证会话上下文
///
/// H5授权重定向场景下, 生成授权链接时将认证所需上下文序列化保存到Redis(以 authToken 为key),
/// 授权回调后凭 authToken 恢复, 供认证策略定位通道应用并完成 code 换 openId/userId。
/// 与 [ChannelAuthService] 的 queryCode 机制(付款码/道通场景)解耦, 独立 key 前缀管理。
@Data
@Accessors(chain = true)
public class AuthSession {

    /// 支付产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 通道商户号
    private String channelMchNo;

    /// 支付能力编码(用于解析具体应用)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    private String capability;

    /// 指定认证应用AppId(可选, 优先级高于配置自动解析)
    private String opAppId;

    /// 来源回跳路径(授权完成后前端回跳的目标路径)
    private String returnPath;
}
