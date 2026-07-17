package cn.daxpay.open.payment.unipay.param;

import cn.daxpay.open.platform.core.validation.IpAddress;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 支付公共参数
///
@Data
@Accessors(chain = true)
@Schema(title = "支付公共参数")
public abstract class PaymentCommonParam {

    /// 客户端ip
    @Schema(description = "客户端ip")
    @IpAddress
    @Size(max=64, message = "{validation.field.clientIp.size}")
    private String clientIp;

    /// 请求ID（商户侧生成，调用追踪与审计索引；参与签名）
    @Schema(description = "请求ID")
    @NotBlank(message = "{validation.field.reqId.notBlank}")
    @Size(max = 64, message = "{validation.field.reqId.size}")
    private String reqId;

    /// 随机数
    @Schema(description = "随机数")
    @Size(max = 32, message = "{validation.field.nonceStr.size}")
    private String nonceStr;

    /// 签名
    @Schema(description = "签名")
    @Size(max = 1024, message = "{validation.field.sign.size}")
    private String sign;

    /// 请求时间（北京时间，格式 yyyy-MM-dd HH:mm:ss）
    @Schema(description = "请求时间(北京时间，yyyy-MM-dd HH:mm:ss)")
    @NotNull(message = "{validation.field.reqTime.notNull}")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime reqTime;

}
