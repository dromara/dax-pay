package org.dromara.daxpay.payment.merchant.result.profile;

import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 商户法人信息结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MchLegalProfileResult extends MchResult {

    @Schema(description = "法人姓名")
    private String legalName;

    @Schema(description = "身份证号")
    private String certNo;

    @Schema(description = "联系人手机号")
    private String contactPhone;

    @Schema(description = "身份证长期有效")
    private boolean periodLong;

    @Schema(description = "身份证开始时间")
    private LocalDate startDate;

    @Schema(description = "身份证结束时间")
    private LocalDate endDate;

    @Schema(description = "身份证地址")
    private String address;

    @Schema(description = "身份证人像面照片")
    private String frontPic;

    @Schema(description = "身份证国徽面照片")
    private String backPic;

}