package cn.daxpay.open.payment.unipay.param.assist;

import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 生成授权链接参数（内部传递，不参与签名/反序列化）
///
/// 认证域统一参数: 由各入口层(调试/网关/开放接口/码牌)解析通道路由后组装传入认证域。
/// 微信应用由入口层 resolve 选定后, 通过 wxAppScope(档位) + wxAppRefId(主键) 标识,
/// 策略层据此调 WxAppFacade.getById 查密钥; 抖音等非微信通道不填。
/// 不继承 PaymentCommonParam — 本类仅用于内部服务间传递, 不对外暴露签名/时间戳等字段。
@Data
@Accessors(chain = true)
@Schema(title = "生成授权链接参数")
public class GenerateAuthUrlParam {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用号(可选)
    @Schema(description = "应用号")
    private String appId;

    /// 通道商户号(抖音等策略用)
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 微信应用档位标识(PLATFORM/MERCHANT)
    ///
    /// 入口层 resolve 选定后填入, 策略层据此调 WxAppFacade.getById 查对应密钥。
    /// 抖音等非微信通道不填。
    /// @see cn.daxpay.open.payment.wx.enums.WxAppScopeEnum
    @Schema(description = "微信应用档位标识")
    private String wxAppScope;

    /// 微信应用主键(与 wxAppScope 配对), 策略层凭此定位具体应用查密钥
    @Schema(description = "微信应用主键")
    private Long wxAppRefId;

    /// 认证类型
    /// @see ChannelAuthTypeEnum
    @Schema(description = "认证类型")
    private String authType;

    /// 来源回跳路径, 授权完成后前端回跳的目标路径, 会随会话码一起保存
    @Schema(description = "来源回跳路径")
    private String returnPath;
}