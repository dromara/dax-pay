package org.dromara.daxpay.core.param;

import cn.bootx.platform.core.validation.IpAddress;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付公共参数
 * @author xxm
 * @since 2023/12/17
 */
@Data
@Schema(title = "支付公共参数")
public abstract class PaymentCommonParam {

    /** 应用号 */
    @Schema(description = "应用号")
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    private String appId;

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
