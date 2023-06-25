package cn.bootx.platform.daxpay.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 支付参数
 *
 * @author xxm
 * @since 2020/12/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付参数")
public class PayParam implements Serializable {

    private static final long serialVersionUID = 3895679513150533566L;

    @Schema(description = "商户编码")
    @NotEmpty(message = "商户应用不可为空")
    private String mchCode;

    @Schema(description = "商户应用编码")
    @NotEmpty(message = "商户应用编码不可为空")
    private String mchAppCode;

    @Schema(description = "业务ID")
    @NotBlank(message = "业务ID不可为空")
    private String businessId;

    @Schema(description = "支付标题")
    @NotBlank(message = "支付标题不可为空")
    private String title;

    @Schema(description = "支付描述")
    private String description;

    @Schema(description = "支付方式信息参数")
    @NotEmpty(message = "支付方式信息参数不可为空")
    @Valid
    private List<PayWayParam> payWayList;

}
