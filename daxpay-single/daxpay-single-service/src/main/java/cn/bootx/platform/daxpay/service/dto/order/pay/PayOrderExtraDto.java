package cn.bootx.platform.daxpay.service.dto.order.pay;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付订单扩展信息")
public class PayOrderExtraDto extends BaseDto {

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 支付终端ip */
    @Schema(description = "支付终端ip")
    private String clientIp;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址，以最后一次为准")
    private String notifyUrl;

    /** 签名类型 */
    @Schema(description = "签名类型")
    private String reqSignType;

    /** 签名，以最后一次为准 */
    @Schema(description = "签名")
    private String reqSign;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数")
    private String attach;

    /** 请求时间，时间戳转时间, 以最后一次为准 */
    @Schema(description = "请求时间，传输时间戳，以最后一次为准")
    private LocalDateTime reqTime;

    /** 错误码 */
    @Schema(description = "错误码")
    private String errorCode;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;
}
