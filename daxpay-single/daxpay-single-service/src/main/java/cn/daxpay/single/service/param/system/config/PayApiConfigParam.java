package cn.daxpay.single.service.param.system.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 支付开放接口管理
 * @author xxm
 * @since 2023/12/22
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付开放接口管理")
public class PayApiConfigParam {


    @NotNull(message = "主键不能为空")
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "是否启用")
    private boolean enable;

    @Size(max = 200, message = "备注长度不能超过200")
    @Schema(description = "备注")
    private String remark;
}
