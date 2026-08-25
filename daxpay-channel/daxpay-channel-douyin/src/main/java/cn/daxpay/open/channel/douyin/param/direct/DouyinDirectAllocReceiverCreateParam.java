package cn.daxpay.open.channel.douyin.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音直连分账接收方新增(绑定)参数
///
/// 提交后同步调通道 addSplitReceiver 完成绑定,
/// 失败时记录保留(状态 fail), 修正后可重新绑定。
@Data
@Accessors(chain = true)
@Schema(title = "抖音直连分账接收方新增参数")
public class DouyinDirectAllocReceiverCreateParam {

    /// 商户号(商户端控制器以登录商户强制覆盖, 必填性由服务内归属校验兜底)
    @Schema(description = "商户号")
    private String mchNo;

    /// 通道商户号
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    private String channelMchNo;

    /// 接收方类型(MERCHANT_ID/PERSONAL_OPENID)
    @Schema(description = "接收方类型")
    @NotBlank(message = "{validation.field.receiverType.notBlank}")
    private String receiverType;

    /// 接收方账号(商户号或 openid, openid 须为所选应用维度)
    @Schema(description = "接收方账号")
    @NotBlank(message = "{validation.field.receiverAccount.notBlank}")
    private String receiverAccount;

    /// 接收方名称(MERCHANT_ID 时必填商户全称)
    @Schema(description = "接收方名称")
    private String receiverName;

    /// 分账关系类型
    @Schema(description = "分账关系类型")
    @NotBlank(message = "{validation.field.relationType.notBlank}")
    private String relationType;

    /// 自定义分账关系名(relationType=CUSTOM 时必填)
    @Schema(description = "自定义分账关系名")
    private String customRelation;

    /// 绑定所用商户档抖音应用 appid
    @Schema(description = "绑定所用抖音应用appid")
    @NotBlank(message = "{validation.field.channelAppId.notBlank}")
    private String channelAppId;
}
