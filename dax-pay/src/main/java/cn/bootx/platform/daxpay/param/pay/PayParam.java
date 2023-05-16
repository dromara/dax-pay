package cn.bootx.platform.daxpay.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author xxm
 * @date 2020/12/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付参数")
public class PayParam implements Serializable {

    private static final long serialVersionUID = 3895679513150533566L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "业务ID")
    @NotEmpty(message = "业务ID不可为空")
    private String businessId;

    @Schema(description = "支付标题")
    @NotEmpty(message = "支付标题不可为空")
    private String title;

    @Schema(description = "支付描述")
    private String description;

    @Schema(description = "支付信息参数", required = true)
    @NotEmpty(message = "支付信息参数不可为空")
    @Valid
    private List<PayModeParam> payModeList;

}
