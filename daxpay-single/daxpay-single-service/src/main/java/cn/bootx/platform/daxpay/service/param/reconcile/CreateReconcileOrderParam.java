package cn.bootx.platform.daxpay.service.param.reconcile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 创建对账订单参数
 * @author xxm
 * @since 2024/1/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "创建对账订单参数")
public class CreateReconcileOrderParam {

    @NotNull(message = "日期不能为空")
    @Schema(description = "日期")
    private LocalDate date;

    @NotBlank(message = "同步的支付通道不能为空")
    @Schema(description = "支付通道")
    private String channel;
}
