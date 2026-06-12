package org.dromara.daxpay.payment.merchant.param.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 持卡人信息参数
///

@Data
@Accessors(chain = true)
public class MchCardHolderProfileParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "持卡人姓名")
    private String holderName;

    @Schema(description = "身份证号")
    private String certNo;

    @Schema(description = "身份证长期有效")
    private boolean periodLong;

    @Schema(description = "身份证开始时间")
    private LocalDate startDate;

    @Schema(description = "身份证结束时间")
    private LocalDate endDate;

    @Schema(description = "身份证人像面照片")
    private String frontPic;

    @Schema(description = "身份证国徽面照片")
    private String backPic;

    @Schema(description = "非法人结算授权函图片")
    private String letterOfAuthPic;

}
