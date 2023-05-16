package cn.bootx.platform.daxpay.param.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 退款参数
 *
 * @author xxm
 * @date 2020/12/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款参数")
public class RefundParam {

    @Schema(description = "业务id")
    private String businessId;

    @Schema(description = "各通道退款参数")
    private List<RefundModeParam> refundModeParams;

}
