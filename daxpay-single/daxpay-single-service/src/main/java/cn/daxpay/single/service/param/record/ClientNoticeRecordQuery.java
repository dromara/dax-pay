package cn.daxpay.single.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 客户通知记录查询
 * @author xxm
 * @since 2024/2/24
 */
@QueryParam
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "客户通知记录查询")
public class ClientNoticeRecordQuery extends QueryOrder {

    /** 任务ID */
    @Schema(description = "任务ID")
    private Long taskId;

    /** 请求次数 */
    @Schema(description = "请求次数")
    private Integer reqCount;

    /** 发送是否成功 */
    @Schema(description = "发送是否成功")
    private Boolean success;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;
}
