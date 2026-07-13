package cn.daxpay.open.payment.channel.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道商户信息查询参数
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户信息查询参数")
public class ChannelMerchantQuery {

    /// 通道商户号
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "通道商户号")
    private String channelMerchantNo;

    /// 通道商户名称
    @Schema(description = "通道商户名称")
    private String channelMerchantName;

    /// 所属通道
    @Schema(description = "所属通道")
    private String channel;

    /// 所属商户
    @Schema(description = "所属商户")
    private String mchNo;

    /// 是否沙箱环境商户
    @Schema(description = "是否沙箱环境商户")
    private Boolean sandbox;

}
