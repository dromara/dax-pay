package cn.bootx.platform.daxpay.param.channel.config;

import cn.bootx.platform.common.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import static cn.bootx.platform.common.core.annotation.QueryParam.CompareTypeEnum.LIKE;

/**
 * 支付通道配置 编码见 @see {@link cn.bootx.platform.daxpay.code.pay.PayChannelCode}
 *
 * @author xxm
 * @date 2023-05-24
 */
@Data
@Schema(title = "支付通道配置")
@Accessors(chain = true)
public class PayChannelConfigParam {

    @Schema(description = "主键")
    private Long id;

    @QueryParam(type = LIKE)
    @Schema(description = "通道编码")
    private String code;

    @QueryParam(type = LIKE)
    @Schema(description = "支付通道名称")
    private String name;

    @Schema(description = "图片")
    private Long image;

    @Schema(description = "排序")
    private Double sortNo;

}
