package cn.daxpay.multi.service.param.reconcile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 对账差异记录查询参数
 * @author xxm
 * @since 2024/8/7
 */
@Data
@Accessors(chain = true)
@Schema(title = "对账差异记录查询参数")
public class ReconcileDiscrepancyQuery {
}
