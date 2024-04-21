package cn.bootx.platform.daxpay.result;

import cn.bootx.platform.daxpay.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付通用返回参数
 * @author xxm
 * @since 2023/12/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付通用返回参数")
public class CommonResult {

    @Schema(description = "响应数据签名值")
    private String sign;

    @Schema(description = "错误码")
    private String code;

    @Schema(description = "错误信息")
    private String msg;

    @Schema(description = "响应时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime resTime;

}
