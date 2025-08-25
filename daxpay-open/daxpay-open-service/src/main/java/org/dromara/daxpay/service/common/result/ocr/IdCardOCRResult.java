package org.dromara.daxpay.service.common.result.ocr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 身份证识别结果
 */
@Data
@Accessors(chain = true)
@Schema(title = "身份证识别结果")
public class IdCardOCRResult {

    /** 身份证号 */
    @Schema(description = "身份证号")
    private String idNumber;

    /** 姓名 */
    @Schema(description = "姓名")
    private String name;

    /** 性别 */
    @Schema(description = "性别")
    private String sex;

    /** 生日 */
    @Schema(description = "生日")
    private String birthDate;

    /** 地址 */
    @Schema(description = "地址")
    private String address;

    /** 民族 */
    @Schema(description = "民族")
    private String nation;

    /** 签发机关 */
    @Schema(description = "签发机关")
    private String issue;

    /** 有效期限 */
    @Schema(description = "有效期限")
    private String expire;

    /** 是否为长期 */
    @Schema(description = "是否为长期")
    private boolean periodLong;

    /** 开始时间 yyyy-MM-dd */
    @Schema(description = "开始时间")
    private String startDate;

    /** 结束时间 yyyy-MM-dd */
    @Schema(description = "结束时间")
    private String endDate;



}
