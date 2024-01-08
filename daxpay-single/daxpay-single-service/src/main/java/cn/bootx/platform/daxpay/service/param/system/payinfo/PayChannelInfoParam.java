package cn.bootx.platform.daxpay.service.param.system.payinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "")
public class PayChannelInfoParam {

    @Schema(description= "主键")
    private Long id;

    /** logo图片 */
    @Schema(description = "logo图片")
    private Long iconId;

    /** 卡牌背景色 */
    @Schema(description = "卡牌背景色")
    private String bgColor;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
