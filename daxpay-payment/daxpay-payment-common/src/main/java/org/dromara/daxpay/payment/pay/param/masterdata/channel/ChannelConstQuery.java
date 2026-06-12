package org.dromara.daxpay.payment.pay.param.masterdata.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付通道查询参数
///
@Data
@Accessors(chain = true)
@Schema(title = "支付通道查询参数")
public class ChannelConstQuery {

    /// 通道编码
    @Schema(description = "通道编码")
    private String code;

    /// 通道名称
    @Schema(description = "通道名称")
    private String name;

    /// 是否支持服务商模式
    @Schema(description = "是否支持服务商模式")
    private Boolean isv;

    /// 是否支持分账
    @Schema(description = "是否支持分账")
    private Boolean allocatable;

    /// 是否支持终端报备
    @Schema(description = "是否支持终端报备")
    private Boolean terminal;

    /// 是否支持进件申请
    @Schema(description = "是否支持进件申请")
    private Boolean apply;

}