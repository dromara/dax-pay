package cn.daxpay.open.channel.mock.result.alloc;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # Mock 通道分账接收方结果
///
/// 字段与微信/支付宝/抖音接收方结果取并集后裁掉应用维度(mock 零资料, 无 appid 概念)。
/// 接收方账号/名称为落库密文解密后的明文, 列表展示时按 @SensitiveInfo 脱敏。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "Mock模拟通道分账接收方结果")
public class MockAllocReceiverResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "接收方类型")
    private String receiverType;

    @Schema(description = "接收方账号(解密后明文, 展示时脱敏)")
    @SensitiveInfo(front = 4, end = 4)
    private String receiverAccount;

    @Schema(description = "接收方名称(解密后明文)")
    private String receiverName;

    @Schema(description = "接收方别名(本地备注, 不上送通道)")
    private String alias;

    @Schema(description = "绑定状态")
    private String status;

    @Schema(description = "最近一次绑定/解绑失败原因")
    private String errorMsg;

    @Schema(description = "绑定成功时间")
    private OffsetDateTime bindTime;

    @Schema(description = "解绑成功时间")
    private OffsetDateTime unbindTime;
}
