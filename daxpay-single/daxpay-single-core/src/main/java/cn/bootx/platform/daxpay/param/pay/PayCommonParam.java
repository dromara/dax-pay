package cn.bootx.platform.daxpay.param.pay;

import cn.bootx.platform.daxpay.serializer.TimestampToLocalDateTimeDeserializer;
import cn.bootx.platform.daxpay.util.PayUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付公共参数
 * @author xxm
 * @since 2023/12/17
 */
@Data
@Schema(title = "支付公共参数")
public abstract class PayCommonParam {

    /** 客户端ip */
    @NotBlank(message = "客户端ip不可为空")
    @Schema(description = "客户端ip")
    private String clientIp;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

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
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime reqTime;


    /**
     * 如果需要进行签名,
     *  1. 参数名ASCII码从小到大排序（字典序）
     *  2. 如果参数的值为空不参与签名
     *  3. 参数名不区分大小写
     *  4. 嵌套对象转换成先转换成MAP再序列化为字符串
     *  5. 支持两层嵌套, 更多层级嵌套未测试, 可能会导致不可预知的问题
     */
    public Map<String,String> toMap(){
        return PayUtil.toMap(this);
    }

}
