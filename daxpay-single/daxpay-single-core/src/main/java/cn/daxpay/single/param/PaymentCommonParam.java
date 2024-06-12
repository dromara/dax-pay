package cn.daxpay.single.param;

import cn.daxpay.single.serializer.TimestampToLocalDateTimeDeserializer;
import cn.bootx.platform.common.core.validation.IpAddress;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 支付公共参数
 * @author xxm
 * @since 2023/12/17
 */
@Data
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


    /** 请求时间，时间戳转时间 */
    @Schema(description = "请求时间，传输时间戳")
    @NotNull(message = "请求时间必填")
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime reqTime;

}
