package cn.bootx.platform.daxpay.param.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 支付公共参数
 * @author xxm
 * @since 2023/12/17
 */
@Data
@Schema(title = "支付公共参数")
public class PayCommonParam {

    /** 客户端ip */
    @NotBlank(message = "客户端ip不可为空")
    @Schema(description = "客户端ip")
    private String clientIp;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String extraParam;

    @Schema(description = "是否不进行同步通知的跳转")
    private boolean notReturn;

    /** 同步通知URL */
    @Schema(description = "同步通知URL")
    private String returnUrl;

    /** 是否不启用异步通知 */
    @Schema(description = "是否不启用异步通知")
    private boolean notNotify;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    /** 签名类型 */
    @Schema(description = "签名类型")
    private String signType;

    /** 签名 */
    @Schema(description = "签名")
    private String sign;

    /** API版本号 */
    @Schema(description = "API版本号")
    @NotBlank(message = "API版本号必填")
    private String version;

    /** 请求时间，时间戳转时间 */
    @Schema(description = "请求时间，传输时间戳")
    @NotNull(message = "请求时间必填")
    private LocalDateTime reqTime;

}
