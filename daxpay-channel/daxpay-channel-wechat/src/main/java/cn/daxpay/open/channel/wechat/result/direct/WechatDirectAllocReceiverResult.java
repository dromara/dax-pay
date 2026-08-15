package cn.daxpay.open.channel.wechat.result.direct;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 微信直连分账接收方结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信直连分账接收方结果")
public class WechatDirectAllocReceiverResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "接收方类型")
    private String receiverType;

    @Schema(description = "接收方账号(解密后明文)")
    private String receiverAccount;

    @Schema(description = "接收方名称(解密后明文)")
    private String receiverName;

    @Schema(description = "分账关系类型")
    private String relationType;

    @Schema(description = "自定义分账关系名")
    private String customRelation;

    @Schema(description = "绑定所用微信应用appid")
    private String channelAppId;

    @Schema(description = "绑定状态")
    private String status;

    @Schema(description = "最近一次绑定/解绑失败原因")
    private String errorMsg;

    @Schema(description = "绑定成功时间")
    private OffsetDateTime bindTime;

    @Schema(description = "解绑成功时间")
    private OffsetDateTime unbindTime;
}
