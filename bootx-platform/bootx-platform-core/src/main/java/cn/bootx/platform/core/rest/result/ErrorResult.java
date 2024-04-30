package cn.bootx.platform.core.rest.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 错误响应类,携带链路追踪标示 trackId
 *
 * @author xxm
 * @since 2021/9/9
 */
@Getter
@Setter
@Schema(title = "错误响应类,携带链路追踪标示 trackId")
public class ErrorResult<T> extends Result<T> {

    /** 链路追踪标示 */
    @Schema(description = "链路追踪标示")
    private String traceId;

    public ErrorResult(int code, String msg, String traceId) {
        super(code, msg);
        this.traceId = traceId;
    }

}
