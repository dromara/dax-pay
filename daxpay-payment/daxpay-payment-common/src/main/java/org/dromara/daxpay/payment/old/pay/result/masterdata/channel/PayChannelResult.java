package org.dromara.daxpay.payment.old.pay.result.masterdata.channel;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付通道
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付通道")
public class PayChannelResult extends MpBaseEntity {

    @Schema(description = "通道编码")
    private String code;

    @Schema(description = "通道名称")
    private String name;

    @Schema(description = "排序")
    private Integer sortNo;

    @Schema(description = "通道介绍")
    private String description;

    @Schema(description = "图标")
    private String icon;
}