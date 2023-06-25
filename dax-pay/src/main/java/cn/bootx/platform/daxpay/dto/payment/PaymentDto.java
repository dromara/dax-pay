package cn.bootx.platform.daxpay.dto.payment;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xxm
 * @since 2020/12/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付记录")
public class PaymentDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 3269223993950227228L;

    @Schema(description = "关联的业务id")
    private String businessId;

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "是否是异步支付")
    private boolean asyncPayMode;

    @Schema(description = "是否是组合支付")
    private boolean combinationPayMode;

    /**
     * @see cn.bootx.platform.daxpay.code.pay.PayChannelEnum#ASYNC_TYPE_CODE
     */
    @Schema(description = "异步支付通道")
    private String asyncPayChannel;

    /**
     * @see PayStatusCode
     */
    @Schema(description = "支付状态")
    private String payStatus;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "可退款余额")
    private BigDecimal refundableBalance;

    @Schema(description = "错误码")
    private String errorCode;

    @Schema(description = "错误信息")
    private String errorMsg;

    /**
     * @see PayChannelInfo
     */
    @Schema(description = "支付通道信息")
    private List<PayChannelInfo> payChannelInfo;

    /**
     * @see RefundableInfo
     */
    @Schema(description = "可退款信息列表")
    private List<RefundableInfo> refundableInfo;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "支付终端ip")
    private String clientIp;

    @Schema(description = "过期时间")
    private LocalDateTime expiredTime;

}
