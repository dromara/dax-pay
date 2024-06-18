package cn.daxpay.single.core.param.payment.allocation;

import cn.daxpay.single.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 开始分账请求参数
 * @author xxm
 * @since 2024/4/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "分账请求参数")
public class AllocationParam extends PaymentCommonParam {

    /** 商户分账单号 */
    @NotBlank(message = "商户分账单号不可为空")
    @Size(max = 100, message = "商户分账单号不可超过100位")
    @Schema(description = "商户分账单号")
    private String bizAllocNo;

    /** 支付订单号 */
    @Size(max = 32, message = "支付订单号不可超过32位")
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 商户支付订单号 */
    @Size(max = 100, message = "商户支付订单号不可超过100位")
    @Schema(description = "商户支付订单号")
    private String bizOrderNo;

    /** 分账描述 */
    @Size(max = 150, message = "分账描述不可超过150位")
    @Schema(description = "分账描述")
    private String description;

    /**
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    @Size(max = 20, message = "分账组编号不可超过20位")
    @Schema(description = "分账组编号")
    private String groupNo;

    /** 分账接收方列表 */
    @Schema(description = "分账接收方列表")
    @Valid
    private List<AllocReceiverParam> receivers;

    /** 回调通知地址 */
    @Size(max = 200, message = "回调通知地址不可超过200位")
    @Schema(description = "回调通知地址")
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    @Size(max = 500, message = "商户扩展参数不可超过500位")
    @Schema(description = "商户扩展参数")
    private String attach;

}
