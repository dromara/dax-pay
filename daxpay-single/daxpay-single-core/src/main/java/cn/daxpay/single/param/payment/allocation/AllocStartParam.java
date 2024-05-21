package cn.daxpay.single.param.payment.allocation;

import cn.daxpay.single.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 开始分账请求参数
 * @author xxm
 * @since 2024/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "开始分账请求参数")
public class AllocStartParam extends PaymentCommonParam {

    /** 商户分账单号 */
    @Schema(description = "商户分账单号")
    @NotBlank(message = "商户分账单号不可为空")
    private String bizAllocationNo;

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /** 分账描述 */
    @Schema(description = "分账描述")
    private String description;

    /**
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    @Schema(description = "分账组编号")
    private String groupNo;

    /** 分账接收方列表 */
    @Schema(description = "分账接收方列表")
    private List<AllocReceiverParam> receivers;

}
