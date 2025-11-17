package org.dromara.daxpay.payment.unipay.param;

import cn.bootx.platform.core.validation.IpAddress;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付公共参数
 * @author xxm
 * @since 2025/8/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付公共参数")
public abstract class PaymentCommonParam {

    /** 客户端ip */
    @Schema(description = "客户端ip")
    @IpAddress
    @Size(max=64, message = "客户端ip不可超过64位")
    private String clientIp;

    /** 随机数 */
    @Schema(description = "随机数")
    @Size(max = 32, message = "随机数不可超过32位")
    private String nonceStr;

    /** 签名 */
    @Schema(description = "签名")
    @Size(max = 64, message = "签名不可超过64位")
    private String sign;

    /** 请求时间 格式yyyy-MM-dd HH:mm:ss */
    @Schema(description = "请求时间, 格式yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "请求时间必填")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime reqTime;

}
