package cn.daxpay.multi.service.result.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户信息
 * @author xxm
 * @since 2024/6/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户信息")
public class MerchantResult {

    /** 商户id */
    @Schema(description = "商户id")
    private Long id;

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 商户名称 */
    @Schema(description = "商户名称")
    private String mchName;

    /** 公司名称 */
    @Schema(description = "公司名称")
    private String companyName;

    /** 证件类型 */
    @Schema(description = "证件类型")
    private String idType;

    /** 证件号 */
    @Schema(description = "证件号")
    private String idNo;

    /** 联系方式 */
    @Schema(description = "联系方式")
    private String contact;

    /** 法人名称 */
    @Schema(description = "法人名称")
    private String legalPerson;

    /** 法人证件号码 */
    @Schema(description = "法人证件号码")
    private String legalPersonIdNo;

    /** 状态 */
    @Schema(description = "状态")
    private String status;
}
