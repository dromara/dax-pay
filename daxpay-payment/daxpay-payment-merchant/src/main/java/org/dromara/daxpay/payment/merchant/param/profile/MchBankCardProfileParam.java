package org.dromara.daxpay.payment.merchant.param.profile;

import org.dromara.daxpay.platform.core.enums.channel.OnbBankAccountTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户结算卡信息参数
///
@Data
@Accessors(chain = true)
public class MchBankCardProfileParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    /// 账户类型
    /// @see OnbBankAccountTypeEnum
    @Schema(description = "账户类型")
    private String accountType;

    @Schema(description = "银行卡账户名")
    private String accountName;

    @Schema(description = "银行卡号")
    private String cardNo;

    @Schema(description = "银行卡开户行名称")
    private String bankName;

    @Schema(description = "银行卡开户行联行号")
    private String branchNo;

    @Schema(description = "银行卡正面照片")
    private String cardFrontPic;

    @Schema(description = "银行卡反面照片")
    private String cardBackPic;
}

