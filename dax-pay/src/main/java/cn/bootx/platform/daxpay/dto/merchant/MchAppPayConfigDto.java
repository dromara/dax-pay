package cn.bootx.platform.daxpay.dto.merchant;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户应用支付配置
 * @author xxm
 * @date 2023-05-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "商户应用支付配置")
@Accessors(chain = true)
public class MchAppPayConfigDto extends BaseDto {

    @Schema(description = "关联配置ID")
    private Long configId;
    @Schema(description = "支付通道类型")
    private String channel;
    @Schema(description = "支付通道名称")
    private String channelName;
    @Schema(description = "状态")
    private String state;
    @Schema(description = "备注")
    private String remark;

}
