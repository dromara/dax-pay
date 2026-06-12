package org.dromara.daxpay.payment.channel.bo.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/// # 进件营业执照信息结果
///
@Data
@Accessors(chain = true)
@Schema(title = "进件营业执照信息结果")
public class OnbLicenseProfileBo {

    @Schema(description = "申请ID")
    private Long applyId;

   
    @Schema(description = "营业执照号")
    private String licenseNo;

    @Schema(description = "营业执照名称")
    private String licenseName;

    @Schema(description = "执照地址-省市区编码")
    private List<String> regionCode;

    @Schema(description = "营业执照详细地址")
    private String address;

    @Schema(description = "营业执照长期有效")
    private boolean periodLong;

    @Schema(description = "营业执照开始日期")
    private LocalDate startDate;

    @Schema(description = "营业执照结束日期")
    private LocalDate endDate;

    @Schema(description = "营业执照照片(媒体ID)")
    private String licensePic;

    @Schema(description = "营业执照照片路径(系统存储)")
    private String licensePicUrl;
}
