package cn.daxpay.open.payment.device.terminal.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.device.enums.TerminalTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.List;

/// # 通道终端台账
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "通道终端台账")
public class ChannelTerminalResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "所属通道")
    private String channel;

    /// 报送类型
    /// @see TerminalTypeEnum
    @Schema(description = "报送类型")
    private String type;

    @Schema(description = "终端名称")
    private String name;

    @Schema(description = "通道侧终端号")
    private String outTerminalNo;

    /// 登记状态
    /// @see ChannelTerminalStatusEnum
    @Schema(description = "登记状态")
    private String status;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "备注")
    private String remark;

    /// 已绑定的系统终端列表(查询时填充, 不落库)
    @Schema(description = "已绑定的系统终端列表")
    private List<TerminalDeviceResult> systemTerminals;
}
