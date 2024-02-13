package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import cn.bootx.platform.daxpay.serializer.TimestampToLocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一下单参数
 *
 * @author xxm
 * @since 2020/12/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付参数")
public class PayParam extends PaymentCommonParam {

    @Schema(description = "业务号")
    @NotBlank(message = "业务号不可为空")
    private String businessNo;

    @Schema(description = "支付标题")
    @NotBlank(message = "支付标题不可为空")
    private String title;

    @Schema(description = "支付描述")
    private String description;

    @Schema(description = "过期时间, 多次传输以第一次为准")
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime expiredTime;

    @Schema(description = "用户付款中途退出返回商户网站的地址(部分支付场景中可用)")
    private String quitUrl;

    @Schema(description = "支付通道信息参数")
    @NotNull(message = "支付通道信息参数不可为空")
    @Valid
    private List<PayChannelParam> payChannels;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    /** 同步跳转URL, 不传输跳转到默认地址 */
    @Schema(description = "同步跳转URL")
    private String returnUrl;

    /** 是否不启用异步通知 */
    @Schema(description = "是否不启用异步通知")
    private boolean notNotify;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    private String notifyUrl;
}
