package cn.daxpay.single.service.param.system.payinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付方式信息")
public class PayWayInfoParam {

    @NotNull(message = "主键不能为空")
    @Schema(description= "主键")
    private Long id;

    @Size(max = 200, message = "备注长度不能超过200")
    @Schema(description = "备注")
    private String remark;
}
