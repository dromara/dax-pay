package cn.daxpay.open.channel.alipay.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝服务商分账接收方新增(绑定)参数
///
/// 提交后同步调通道 alipay.trade.royalty.relation.bind(服务商代调用, app_auth_token
/// 取自子商户授权绑定, 凭证组装全自动) 完成绑定, 失败时记录保留(状态 fail), 修正后可重新绑定。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商分账接收方新增参数")
public class AlipayIsvAllocReceiverCreateParam {

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
}
