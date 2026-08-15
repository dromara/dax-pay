package cn.daxpay.open.channel.wechat.param.isv;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信服务商分账接收方查询参数
///
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商分账接收方查询参数")
public class WechatIsvAllocReceiverQuery {

    /// 通道商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "通道商户号")
    private String channelMchNo;

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
