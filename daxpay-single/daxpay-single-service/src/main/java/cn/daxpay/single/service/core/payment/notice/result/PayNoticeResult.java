package cn.daxpay.single.service.core.payment.notice.result;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.PayStatusEnum;
import cn.daxpay.single.result.PaymentCommonResult;
import cn.daxpay.single.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付异步通知类
 * @author xxm
 * @since 2024/1/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付异步通知类")
public class PayNoticeResult extends PaymentCommonResult {

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 支付方式 */
    @Schema(description = "支付方式")
    private String method;

    /** 支付金额 */
    @Schema(description = "支付金额")
    private Integer amount;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;

    /** 支付成功时间 */
    @Schema(description = "支付成功时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime payTime;

    /** 支付关闭时间 */
    @Schema(description = "支付关闭时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime closeTime;

    /** 支付创建时间 */
    @Schema(description = "支付创建时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime createTime;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

}
