package cn.daxpay.single.service.param.system.payinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付通道配置
 * @author xxm
 * @since 2024/1/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付通道配置")
public class PayChannelInfoParam {

    @Schema(description= "主键")
    private Long id;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
