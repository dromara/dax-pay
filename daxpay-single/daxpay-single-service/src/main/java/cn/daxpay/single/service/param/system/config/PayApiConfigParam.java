package cn.daxpay.single.service.param.system.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付开放接口管理
 * @author xxm
 * @since 2023/12/22
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付开放接口管理")
public class PayApiConfigParam {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "是否启用")
    private boolean enable;

    @Schema(description = "备注")
    private String remark;
}
