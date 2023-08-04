package cn.bootx.platform.daxpay.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算台发起支付参数
 *
 * @author xxm
 * @since 2022/2/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "结算台单支付参数")
public class CashierSinglePayParam {

    @Schema(description = "商户编码")
    private String mchCode;

    @Schema(description = "商户应用编码")
    private String mchAppCode;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "openId")
    private String openId;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "支付方式")
    private String payWay;

    @Schema(description = "业务id")
    private String businessId;

    @Schema(description = "付款码")
    private String authCode;

    @Schema(description = "储值卡")
    private String voucherNo;

    @Schema(description = "储值卡")
    private List<String> voucherNoList;

    @Schema(description = "钱包ID")
    private String walletId;

}
