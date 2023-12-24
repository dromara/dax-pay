package cn.bootx.platform.daxpay.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一下单参数
 *
 * @author xxm
 * @since 2020/12/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付参数")
public class PayParam extends PayCommonParam{

    @Schema(description = "业务号")
    @NotBlank(message = "业务号不可为空")
    private String businessNo;

    @Schema(description = "支付标题")
    @NotBlank(message = "支付标题不可为空")
    private String title;

    @Schema(description = "支付描述")
    private String description;

    /** 过期时间 */
    @Schema(description = "过期时间")
    private LocalDateTime expiredTime;

    @Schema(description = "用户付款中途退出返回商户网站的地址(部分支付场景中可用)")
    private String quitUrl;

    @Schema(description = "支付方式信息参数")
    @NotEmpty(message = "支付方式信息参数不可为空")
    @Valid
    private List<PayWayParam> payWays;
}
