package cn.daxpay.open.channel.alipay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝直连分账接收方新增(绑定)参数
///
/// 提交后同步调通道 alipay.trade.royalty.relation.bind 完成绑定,
/// 失败时记录保留(状态 fail), 修正后可重新绑定。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连分账接收方新增参数")
public class AlipayDirectAllocReceiverCreateParam {

    /// 商户号(商户端控制器以登录商户强制覆盖, 必填性由服务内归属校验兜底)
    @Schema(description = "商户号")
    private String mchNo;

    /// 通道商户号
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    private String channelMchNo;

    /// 接收方类型(USER_ID/LOGIN_NAME)
    @Schema(description = "接收方类型")
    @NotBlank(message = "{validation.field.receiverType.notBlank}")
    private String receiverType;

    /// 接收方账号(userId 为 2088 开头, loginName 为手机号/邮箱)
    @Schema(description = "接收方账号")
    @NotBlank(message = "{validation.field.receiverAccount.notBlank}")
    private String receiverAccount;

    /// 接收方名称(可空)
    @Schema(description = "接收方名称")
    private String receiverName;

    /// 发起绑定的支付宝应用引用(alipay_direct_app 主键)
    @Schema(description = "发起绑定的支付宝应用引用")
    @NotNull(message = "{validation.field.appRefId.notNull}")
    private Long appRefId;
}
