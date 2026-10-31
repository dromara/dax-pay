package cn.daxpay.open.channel.mock.param.alloc;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 通道分账接收方查询参数
///
@Schema(title = "Mock模拟通道分账接收方查询参数")
@Data
@Accessors(chain = true)
public class MockAllocReceiverQuery {

    /// 通道商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道商户号")
    private String channelMchNo;

    /// 所属支付产品(mock_pay/mock_isv)
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "所属支付产品")
    private String product;

    /// 接收方类型
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "接收方类型")
    private String receiverType;

    /// 绑定状态
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "绑定状态")
    private String status;

    /// 所属商户
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "所属商户")
    private String mchNo;
}
