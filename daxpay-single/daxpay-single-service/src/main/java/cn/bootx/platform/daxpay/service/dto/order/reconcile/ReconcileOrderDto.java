package cn.bootx.platform.daxpay.service.dto.order.reconcile;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 对账订单
 * @author xxm
 * @since 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账订单")
public class ReconcileOrderDto extends BaseDto {

    @Schema(description = "日期")
    private LocalDate date;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "是否下载成功")
    private boolean down;

    @Schema(description = "是否比对完成")
    private boolean compare;

    @Schema(description = "错误信息")
    private String errorMsg;
}
