package org.dromara.daxpay.payment.channel.bo.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/// # 进件法人信息结果
///
@Data
@Accessors(chain = true)
@Schema(title = "进件法人信息结果")
public class OnbLegalProfileBo {

    @Schema(description = "进件申请Id")
    private Long applyId;

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

    @Schema(description = "身份证人像面照片(媒体ID)")
    private String frontPic;

    @Schema(description = "身份证人像面照片路径(系统存储)")
    private String frontPicUrl;

    @Schema(description = "身份证国徽面照片(媒体ID)")
    private String backPic;

    @Schema(description = "身份证国徽面照片路径(系统存储)")
    private String backPicUrl;
}
