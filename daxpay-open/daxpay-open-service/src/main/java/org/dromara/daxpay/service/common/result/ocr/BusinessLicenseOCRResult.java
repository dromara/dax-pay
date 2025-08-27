package org.dromara.daxpay.service.common.result.ocr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 营业执照识别结果
 */
@Data
@Accessors(chain = true)
@Schema(title = "营业执照识别结果")
public class BusinessLicenseOCRResult {

    /**
     * 公司名称
     */
    @Schema(description = "公司名称")
    private String name;

    /**
     * 公司编号
     */
    @Schema(description = "公司编号")
    private String number;

    /**
     * 注册地址
     */
    @Schema(description = "注册地址")
    private String address;

    /**
     * 法人
     */
    @Schema(description = "法人")
    private String legalPerson;


    /**
     * 是否为长期
     */
    @Schema(description = "是否为长期")
    private boolean periodLong;

    /**
     * 营业期开始时间 yyyy-MM-dd
     */
    @Schema(description = "营业期开始时间")
    private String startDate;

    /**
     * 营业期结束时间 yyyy-MM-dd
     */
    @Schema(description = "营业期结束时间")
    private String endDate;

    /**
     * 公司类型
     */
    @Schema(description = "公司类型")
    private String companyType;

    /**
     * 经营范围
     */
    @Schema(description = "经营范围")
    private String businessScope;


    /**
     * 注册时间
     */
    @Schema(description = "注册时间")
    private String registeredDate;

    /**
     * 证件签发时间
     */
    @Schema(description = "证件签发时间")
    private String issueDate;

}
