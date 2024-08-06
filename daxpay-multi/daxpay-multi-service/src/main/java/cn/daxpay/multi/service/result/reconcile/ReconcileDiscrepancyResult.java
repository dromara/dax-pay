package cn.daxpay.multi.service.result.reconcile;

import cn.daxpay.multi.core.result.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 对账差异记录
 * @author xxm
 * @since 2024/8/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "对账差异记录")
public class ReconcileDiscrepancyResult extends MchResult {
}
