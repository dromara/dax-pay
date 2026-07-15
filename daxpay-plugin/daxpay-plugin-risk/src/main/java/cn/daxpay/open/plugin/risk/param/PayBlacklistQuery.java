package cn.daxpay.open.plugin.risk.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 黑名单查询参数
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Accessors(chain = true)
@Schema(title = "黑名单查询参数")
public class PayBlacklistQuery {

    @Schema(description = "类型")
    private String type;

    @Schema(description = "名单值")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String value;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "通道族")
    private String channel;
}
