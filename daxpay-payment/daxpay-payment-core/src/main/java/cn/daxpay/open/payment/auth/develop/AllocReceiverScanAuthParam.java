package cn.daxpay.open.payment.auth.develop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 分账接收方扫码授权参数
///
/// 接收方报备表单扫码获取 openId/userId 用, 不继承支付公共参数(无 reqTime/sign 等字段)。
/// 应用直接以原始 appId 字符串传入(与接收方报备参数同构), 由
/// [AllocReceiverScanAuthService] 解析为应用主键后委托认证域建会话。
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方扫码授权参数")
public class AllocReceiverScanAuthParam {

    /// 商户号(运营端必填, 初始化商户上下文; 商户端控制器以登录商户强制覆盖后校验在服务层兜底;
    /// 支付宝平台级授权实际不使用但统一必填)
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    @Schema(description = "商户号")
    private String mchNo;

    /// 通道商户号(接收方报备所属通道商户, 透传授权会话)
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 支付产品编码(必填, 决定授权通道与所用的应用字段)
    @NotBlank(message = "{validation.field.product.notBlank}")
    @Schema(description = "支付产品编码(wechat_pay/wechat_isv/alipay/alipay_isv/douyin_pay)")
    private String product;

    /// 接收方类型(必填, PERSONAL_OPENID/PERSONAL_SUB_OPENID/USER_ID 扫码可获取,
    /// MERCHANT_ID/LOGIN_NAME 不支持扫码)
    @Schema(description = "接收方类型")
    private String receiverType;

    /// 绑定应用原始 appId(微信直连/抖音, openid 为该应用维度)
    @Schema(description = "绑定应用appId")
    private String channelAppId;

    /// 服务商应用 appId(微信服务商, PERSONAL_OPENID 为该应用维度)
    @Schema(description = "服务商应用appId")
    private String spAppId;

    /// 子商户应用 appId(微信服务商, PERSONAL_SUB_OPENID 为该应用维度)
    @Schema(description = "子商户应用appId")
    private String subAppId;
}
