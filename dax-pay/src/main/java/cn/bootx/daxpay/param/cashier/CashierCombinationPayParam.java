package cn.bootx.daxpay.param.cashier;

import cn.bootx.daxpay.param.pay.PayModeParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算台发起支付参数
 *
 * @author xxm
 * @date 2022/2/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "结算台组合支付参数")
public class CashierCombinationPayParam {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "业务id")
    private String businessId;

    @Schema(description = "支付信息", required = true)
    private List<PayModeParam> payModeList;

}
