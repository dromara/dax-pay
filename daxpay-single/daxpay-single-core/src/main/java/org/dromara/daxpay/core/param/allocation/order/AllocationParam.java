package org.dromara.daxpay.core.param.allocation.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.param.PaymentCommonParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分账请求参数
 * @author xxm
 * @since 2024/11/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
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

    /** 分账标题 */
    @Size(max = 100, message = "分账描述不可超过100位")
    @Schema(description = "分账标题")
    private String title;

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
    private List<ReceiverParam> receivers;

    /** 回调通知地址 */
    @Size(max = 200, message = "回调通知地址不可超过200位")
    @Schema(description = "回调通知地址")
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    @Size(max = 500, message = "商户扩展参数不可超过500位")
    @Schema(description = "商户扩展参数")
    private String attach;

    /**
     * 分账接收方列表
     */
    @Data
    @Accessors(chain = true)
    @Schema(title = "分账接收方列表")
    public static class ReceiverParam {

        /** 分账接收方编号 */
        @Schema(description = "分账接收方编号")
        @NotBlank(message = "分账接收方编号必填")
        @Size(max = 32, message = "分账接收方编号不可超过32位")
        private String receiverNo;

        /** 分账金额 */
        @Schema(description = "分账金额")
        @NotNull(message = "分账金额必填")
        @Min(value = 1,message = "分账金额至少为0.01元")
        private BigDecimal amount;
    }

}
