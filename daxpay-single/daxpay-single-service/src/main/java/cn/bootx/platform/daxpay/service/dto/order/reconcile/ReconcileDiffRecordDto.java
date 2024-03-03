package cn.bootx.platform.daxpay.service.dto.order.reconcile;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 对账差异单
 * @author xxm
 * @since 2024/3/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账差异单")
public class ReconcileDiffRecordDto extends BaseDto {
}
