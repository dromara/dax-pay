package org.dromara.daxpay.sdk.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息通知通用响应对象
 * @author xxm
 * @since 2025/4/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "消息通知通用响应对象")
public class DaxNoticeResult<T> extends DaxResult<T>{

    /** 通知类型 */
    @Schema(description = "通知类型")
    private String noticeType;

    /** 应用ID */
    @Schema(description = "应用ID")
    private String appId;
}
