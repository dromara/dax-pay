package cn.daxpay.single.param.payment.transfer;

import cn.daxpay.single.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

/**
 * 转账状态同步参数
 * @author xxm
 * @since 2024/6/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "转账状态同步参数")
public class TransferSyncParam extends PaymentCommonParam {

    /** 商户转账号 */
    @Size(max = 100, message = "商户转账号不可超过100位")
    @Schema(description = "商户转账号")
    private String bizTransferNo;

    /** 转账号 */
    @Size(max = 32, message = "转账号不可超过100位")
    @Schema(description = "转账号")
    private String transferNo;
}
