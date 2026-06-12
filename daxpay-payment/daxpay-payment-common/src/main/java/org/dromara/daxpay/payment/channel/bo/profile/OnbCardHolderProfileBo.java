package org.dromara.daxpay.payment.channel.bo.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 进件持卡人信息结果
///
@Data
@Accessors(chain = true)
@Schema(title = "进件持卡人信息结果")
public class OnbCardHolderProfileBo {

    @Schema(description = "进件申请Id")
    private Long applyId;

   
    @Schema(description = "持卡人姓名")
    private String holderName;

    @Schema(description = "身份证号")
    private String certNo;

    @Schema(description = "持卡人手机号")
    private String cardHolderPhone;

    @Schema(description = "持卡人邮箱")
    private String cardHolderEmail;

    @Schema(description = "身份证长期有效")
    private boolean periodLong;

    @Schema(description = "身份证开始时间")
    private LocalDate startDate;

    @Schema(description = "身份证结束时间")
    private LocalDate endDate;

    @Schema(description = "身份证人像面照片(媒体ID)")
    private String frontPic;

    @Schema(description = "身份证人像面照片路径(系统存储)")
    private String frontPicUrl;

    @Schema(description = "身份证国徽面照片(媒体ID)")
    private String backPic;

    @Schema(description = "身份证国徽面照片路径(系统存储)")
    private String backPicUrl;

    @Schema(description = "非法人结算授权函图片(媒体ID)")
    private String letterOfAuthPic;

    @Schema(description = "非法人结算授权函图片路径(系统存储)")
    private String letterOfAuthPicUrl;
}
