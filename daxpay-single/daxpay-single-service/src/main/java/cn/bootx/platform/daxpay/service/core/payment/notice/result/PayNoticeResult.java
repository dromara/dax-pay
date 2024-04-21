package cn.bootx.platform.daxpay.service.core.payment.notice.result;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付异步通知类
 * @author xxm
 * @since 2024/1/7
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付异步通知类")
public class PayNoticeResult {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "商户订单号")
    private String bizOrderNo;

    @Schema(description = "标题")
    private String title;

    /**
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式")
    private String method;

    @Schema(description = "支付金额")
    private Integer amount;

    /**
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;


    @Schema(description = "支付成功时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime payTime;

    @Schema(description = "支付关闭时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime closeTime;

    @Schema(description = "支付创建时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime createTime;

    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    @Schema(description = "签名")
    private String sign;

}
