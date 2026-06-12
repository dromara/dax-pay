package org.dromara.daxpay.payment.channel.bo.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 进件银行账户信息结果
///
@Data
@Accessors(chain = true)
@Schema(title = "进件银行账户信息结果")
public class OnbBankCardProfileBo {

    @Schema(description = "进件申请Id")
    private Long applyId;

    @Schema(description = "账户类型")
    private String accountType;

    @Schema(description = "开户银行")
    private String bankName;

    @Schema(description = "账户名称")
    private String accountName;

    @Schema(description = "银行卡号")
    private String cardNo;

    @Schema(description = "银行卡开户行联行号")
    private String branchNo;

    @Schema(description = "银行预留手机号")
    private String bankPhone;

    @Schema(description = "银行卡正面照片(媒体ID)")
    private String cardFrontPic;

    @Schema(description = "银行卡正面照片路径(系统存储)")
    private String cardFrontPicUrl;

    @Schema(description = "银行卡反面照片")
    private String cardBackPic;

    @Schema(description = "银行卡反面照片地址")
    private String cardBackPicUrl;
}
