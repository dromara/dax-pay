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
}
