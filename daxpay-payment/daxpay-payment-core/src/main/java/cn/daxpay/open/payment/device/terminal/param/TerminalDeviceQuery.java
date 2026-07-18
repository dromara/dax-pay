package cn.daxpay.open.payment.device.terminal.param;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 系统终端查询
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "系统终端查询")
public class TerminalDeviceQuery {

    @Schema(description = "商户号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String mchNo;

    @Schema(description = "系统终端编码")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String terminalNo;

    @Schema(description = "终端名称")
    private String name;

    @Schema(description = "门店号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String storeNo;

    @Schema(description = "是否启用")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private Boolean enable;
}
