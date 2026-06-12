package org.dromara.daxpay.payment.merchant.result.profile;

import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import org.dromara.daxpay.platform.core.enums.channel.OnbBankAccountTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户结算卡信息结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MchBankCardProfileResult extends MchResult {

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

