package cn.daxpay.single.sdk.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 响应参数接收类
 * @author xxm
 * @since 2024/2/2
 */
@Getter
@Setter
@ToString
public class DaxPayResult<T> {

    /** 状态码 */
    @Schema(description = "状态码")
    private int code;

    /** 提示信息 */
    @Schema(description = "提示信息")
    private String msg;

    /** 业务内容 */
    @Schema(description = "业务内容")
    private T data;

    /** 签名 */
    @Schema(description = "签名")
    private String sign;

    /** 响应时间 */
    @Schema(description = "响应时间")
    private LocalDateTime resTime;

    /** 追踪ID */
    @Schema(description = "追踪ID")
    private String traceId;

}
