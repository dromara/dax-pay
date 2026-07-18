package cn.daxpay.open.payment.device.terminal.param;

import cn.daxpay.open.payment.device.enums.TerminalTypeEnum;
import cn.daxpay.open.platform.core.annotation.QueryParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道终端查询
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "通道终端查询")
public class ChannelTerminalQuery {

    @Schema(description = "商户号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String mchNo;

    @Schema(description = "通道商户号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String channelMchNo;

    @Schema(description = "所属通道")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String channel;

    @Schema(description = "所属支付产品")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String product;

    /// 报送类型
    /// @see TerminalTypeEnum
    @Schema(description = "报送类型")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String type;

    @Schema(description = "终端名称")
    private String name;

    @Schema(description = "通道侧终端号")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String outTerminalNo;

    /// 登记状态
    /// @see ChannelTerminalStatusEnum
    @Schema(description = "登记状态")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String status;
}
