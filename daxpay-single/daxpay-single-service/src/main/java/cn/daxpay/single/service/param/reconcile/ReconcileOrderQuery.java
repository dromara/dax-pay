package cn.daxpay.single.service.param.reconcile;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 对账订单查询参数
 * @author xxm
 * @since 2024/1/22
 */
@QueryParam(type = QueryParam.CompareTypeEnum.EQ)
@Data
@Accessors(chain = true)
@Schema(title = "对账订单查询参数")
public class ReconcileOrderQuery {

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "日期")
    @DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private LocalDate date;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "是否下载成功")
    private Boolean down;

    @Schema(description = "是否比对完成")
    private Boolean compare;

    @Schema(description = "错误信息")
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    private String errorMsg;
}
