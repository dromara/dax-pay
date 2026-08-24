package cn.daxpay.open.payment.unipay.param.trade.transfer;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/// # 统一转账发起参数
///
/// 按通道直连发起转账(商户转账到余额/银行卡/OpenId 等)。
/// 幂等维度为 通道 + 商户转账号 + 商户号: 同组合重复发起会拦截, 失败单可复用原单号重试。
/// 与内部编排参数 [cn.daxpay.open.payment.trade.transfer.param.TransferParam] 同名但职责不同:
/// 本类是对外签名 DTO(含 mchNo/appId/sign, 金额单位为分), Controller 层负责转换(内部金额单位为元)。
/// 通道差异校验(微信金额档位/收款人类型匹配等)下沉到通道策略 doValidateParam, 由核心建单时兜底。
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "统一转账参数")
public class TransferParam extends MerchantPaymentCommonParam {

    /// 转账通道(wechat/alipay/douyin)
    @Schema(description = "转账通道")
    @NotBlank(message = "{validation.field.channel.notBlank}")
    @Pattern(regexp = "wechat|alipay|douyin", message = "{validation.field.channel.pattern}")
    private String channel;

    /// 通道商户号(转账凭证组装与通道路由用, 覆盖基类可选语义为必填)
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    private String channelMchNo;

    /// 商户转账号(幂等键, 同一商户同一通道下唯一; 失败后可复用原单号重试)
    @Schema(description = "商户转账号")
    @NotBlank(message = "{validation.field.bizTransferNo.notBlank}")
    @Size(max = 100, message = "{validation.field.bizTransferNo.size}")
    private String bizTransferNo;

    /// 转账金额(单位: 分, 最小货币单位)
    @Schema(description = "转账金额(分)")
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    @Min(value = 1, message = "{validation.field.amount.min}")
    private Long amount;

    /// 转账标题
    @Schema(description = "转账标题")
    @Size(max = 100, message = "{validation.field.title.size}")
    private String title;

    /// 转账原因/备注
    @Schema(description = "转账原因/备注")
    @Size(max = 200, message = "{validation.field.transferReason.size}")
    private String reason;

    /// 收款人账号类型(微信=openid; 支付宝=user_id/open_id/login_name; 抖音=openid/phone)
    /// @see cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum
    @Schema(description = "收款人账号类型")
    @NotBlank(message = "{validation.field.payeeType.notBlank}")
    @Size(max = 32, message = "{validation.field.payeeType.size}")
    private String payeeType;

    /// 收款人账号
    @Schema(description = "收款人账号")
    @NotBlank(message = "{validation.field.payeeAccount.notBlank}")
    @Size(max = 100, message = "{validation.field.payeeAccount.size}")
    private String payeeAccount;

    /// 收款人姓名(微信: 小于0.3元禁填, 大于等于2000元必填)
    @Schema(description = "收款人姓名")
    @Size(max = 100, message = "{validation.field.transferPayeeName.size}")
    private String payeeName;

    /// 商户扩展参数, 回调时原样返回
    @Schema(description = "商户扩展参数, 回调时原样返回")
    @Size(max = 500, message = "{validation.field.attach.size}")
    private String attach;

    /// 回调通知地址
    @Schema(description = "回调通知地址")
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    private String notifyUrl;

    /// 转账场景报备信息(微信转账场景必填, 各场景要求不同, 留空由通道兜底)
    @Schema(description = "转账场景报备信息")
    @Valid
    private List<ReportInfoParam> reportInfos;

    /// 转账场景标识(支付宝=转账场景配置ID, 抖音=主数据枚举码如1001; 微信不传, 用通道商户配置场景)
    @Schema(description = "转账场景标识")
    @Size(max = 32, message = "{validation.field.transferScene.size}")
    private String transferScene;

    /// 转账场景报备信息项(微信协议: 活动名称/搬运信息类型等)
    @Data
    @Schema(title = "转账场景报备信息项")
    public static class ReportInfoParam {

        /// 信息类型(微信协议固定中文, 如: 活动名称)
        @Schema(description = "信息类型")
        @Size(max = 64, message = "{validation.field.infoType.size}")
        private String infoType;

        /// 信息内容(商户自定义填写)
        @Schema(description = "信息内容")
        @Size(max = 200, message = "{validation.field.infoContent.size}")
        private String infoContent;
    }
}
