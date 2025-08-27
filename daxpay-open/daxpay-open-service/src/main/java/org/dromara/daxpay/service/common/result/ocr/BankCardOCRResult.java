package org.dromara.daxpay.service.common.result.ocr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 银行卡识别结果
 */
@Data
@Accessors(chain = true)
@Schema(title = "银行卡识别结果")
public class BankCardOCRResult {

    @Schema(description = "银行名称")
    private String  bankName;

    @Schema(description = "银行卡号")
    private String cardNumber;

    @Schema(description = "银行卡类型")
    private String cardType;

    @Schema(description = "有效期")
    private String validToDate;
}
