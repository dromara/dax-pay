package cn.daxpay.open.payment.auth.core;

import lombok.Data;
import lombok.experimental.Accessors;
import cn.daxpay.open.payment.auth.channel.MerchantChannelAuthService;

/// # 认证会话上下文
///
/// H5授权重定向场景下, 生成授权链接时将认证所需上下文序列化保存到Redis(以 authToken 为key),
/// 授权回调后凭 authToken 恢复, 供认证策略定位通道应用并完成 code 换 openId/userId。
/// 与 [MerchantChannelAuthService] 的 queryCode 机制(付款码/道通场景)解耦, 独立 key 前缀管理。
///
    /// ## 应用引用
    /// session 只存通道应用的主键引用, 不存明文密钥:
    /// - 微信: wxAppScope + wxAppRefId, 由 [cn.daxpay.open.payment.auth.channel.WechatAuthStrategy] 写入
    /// - 抖音: dyAppScope + dyAppRefId, 由 [cn.daxpay.open.payment.auth.channel.DouyinAuthStrategy] 写入
    /// doAuth 时策略据 scope + refId 调 Facade.getById 反查密钥。
@Data
@Accessors(chain = true)
public class AuthSession {

    /// 认证来源
    ///
    /// 标识本次认证的配置来源, 值为 [AuthSourceEnum#getCode]:
    /// - 平台级配置(调试场景): 见 [AuthSourceEnum] 各枚举项
    /// - null/空: 走商户级支付产品策略(直连/服务商)
    private String source;

    /// 商户号(doAuth 时恢复租户上下文)
    private String mchNo;

    /// 通道商户号(非微信通道如抖音的回调场景恢复用)
    private String channelMchNo;

    /// 认证类型(回调时恢复策略路由)
    /// @see cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum
    private String authType;

    /// 微信应用档位(PLATFORM/MERCHANT), 与 wxAppRefId 配对, 回调时策略据其 getById 反查密钥
    /// @see cn.daxpay.open.payment.wx.enums.WxAppScopeEnum
    private String wxAppScope;

    /// 微信应用主键, 与 wxAppScope 配对, 回调时策略据其 getById 反查密钥
    private Long wxAppRefId;

    /// 抖音应用档位(PLATFORM/MERCHANT), 与 dyAppRefId 配对, 回调时策略据其 getById 反查密钥
    /// @see cn.daxpay.open.payment.douyin.enums.DyAppScopeEnum
    private String dyAppScope;

    /// 抖音应用主键, 与 dyAppScope 配对, 回调时策略据其 getById 反查密钥
    private Long dyAppRefId;

    /// 来源回跳路径(授权完成后前端回跳的目标路径)
    private String returnPath;

    /// 认证场景
    ///
    /// 标识本次认证的业务目的:
    /// - [AuthScene#PAYMENT]: 支付认证(签名API + 网关H5)
    /// - [AuthScene#PLATFORM]: 平台自用认证(调试/通知/社交登录)
    /// - [AuthScene#OPEN]: 对外开放认证(重定向获取用户标识)
    ///
    /// 由各场景入口在创建会话时写入, 供回调处理时区分结果返回方式(JSON vs 302重定向)。
    private String scene;

    /// 查询码(调试轮询用)
    ///
    /// OAuth 重定向场景下, 回调 URL 仅含 authToken(第三方不透传 queryCode),
    /// 故将 queryCode 随会话保存, auth 时从会话恢复并据此把结果写回轮询 redis。
    private String queryCode;
}