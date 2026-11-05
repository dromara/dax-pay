package cn.daxpay.open.payment.trade.transfer.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 转账发起参数(管理端/商户端共用)
///
/// 各通道差异校验下沉到通道策略 [cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy]，
/// 如微信要求 [payeeType]=openid、金额档位姓名校验等。
@Data
@Accessors(chain = true)
@Schema(title = "转账发起参数")
public class TransferParam {

    /// 商户号(运营端代发必填; 商户端由登录上下文强制, 忽略入参)
    @Size(max = 32, message = "{validation.field.mchNo.size}")
    @Schema(description = "商户号")
    private String mchNo;

    /// 应用ID(unipay 轨由签名参数透传, 出站通知派发用; 管理端/商户端可不传)
    @Size(max = 64, message = "{validation.field.transferAppId.size}")
    @Schema(description = "应用ID")
    private String appId;

    /// 通道商户号(凭证组装与通道路由用)
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Size(max = 64, message = "{validation.field.transferChannelMchNo.size}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 商户转账号(幂等键, 同一应用下唯一; FAIL 后复用原单号重试)
    @NotBlank(message = "{validation.field.bizTransferNo.notBlank}")
    @Size(max = 100, message = "{validation.field.bizTransferNo.size}")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /// 转账金额(分, 与内部金额单位统一)
    @NotNull(message = "{validation.field.transferAmount.notNull}")
    @Min(value = 1, message = "{validation.field.transferAmount.min}")
    @Schema(description = "转账金额(分)")
    private Long amount;

    /// 转账标题
    @Size(max = 100, message = "{validation.field.transferTitle.size}")
    @Schema(description = "转账标题")
    private String title;

    /// 转账原因/备注
    @Size(max = 200, message = "{validation.field.transferReason.size}")
    @Schema(description = "转账原因/备注")
    private String reason;

    /// 收款人账号类型
    /// @see cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum
    /// 抖音支持 openid/phone 两种(手机号复用收款人账号字段, 子应用证书加密上送)
    @NotBlank(message = "{validation.field.payeeType.notBlank}")
    @Size(max = 32, message = "{validation.field.payeeType.size}")
    @Schema(description = "收款人账号类型")
    private String payeeType;

    /// 收款人账号
    @NotBlank(message = "{validation.field.payeeAccount.notBlank}")
    @Size(max = 100, message = "{validation.field.payeeAccount.size}")
    @Schema(description = "收款人账号")
    private String payeeAccount;

    /// 收款人姓名(微信: 小于0.3元禁填, 大于等于2000元必填)
    @Size(max = 100, message = "{validation.field.transferPayeeName.size}")
    @Schema(description = "收款人姓名")
    private String payeeName;

    /// 商户扩展参数, 回调时原样返回
    @Size(max = 500, message = "{validation.field.attach.size}")
    @Schema(description = "商户扩展参数, 回调时原样返回")
    private String attach;

    /// 回调通知地址
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    @Schema(description = "回调通知地址")
    private String notifyUrl;

    /// 转账场景报备信息(微信转账必填, 各场景要求不同, 留空由通道兜底)
    @Schema(description = "转账场景报备信息")
    private List<TransferReportInfo> reportInfos;

    /// 转账场景标识(三通道通用: 支付宝=转账场景配置ID, 抖音=主数据枚举码如1001, 发起时由前端选择传入; 微信不传, 用通道商户配置场景)
    @Size(max = 32, message = "{validation.field.transferScene.size}")
    @Schema(description = "转账场景标识(支付宝=场景配置ID/抖音=场景枚举码, 微信不传用配置)")
    private String transferScene;
}
