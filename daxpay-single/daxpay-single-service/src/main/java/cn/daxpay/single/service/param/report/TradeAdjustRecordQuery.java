package cn.daxpay.single.service.param.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 交易调整记录查询参数
 * @author xxm
 * @since 2024/7/15
 */
@Data
@Accessors(chain = true)
@Schema(title = "交易调整记录查询参数")
public class TradeAdjustRecordQuery {
}
