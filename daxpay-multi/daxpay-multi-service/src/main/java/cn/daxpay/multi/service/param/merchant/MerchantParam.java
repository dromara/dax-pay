package cn.daxpay.multi.service.param.merchant;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户参数
 * @author xxm
 * @since 2024/6/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户参数")
public class MerchantParam {

    /** 主键ID */
    @Schema(description = "主键ID")
    @NotNull(message = "主键ID不可为空", groups = ValidationGroup.edit.class)
    private Long id;

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 商户名称 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "商户名称")
    private String mchName;

    /** 公司名称 */
    @NotBlank(message = "公司名称不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "公司名称")
    private String companyName;

    /** 公司联系方式 */
    @Schema(description = "公司联系方式")
    private String companyContact;

    /** 公司信用编码 */
    @Schema(description = "公司信用编码")
    private String companyCode;

    /** 公司地址 */
    @Schema(description = "公司地址")
    private String companyAddress;

    /** 法人名称 */
    @Schema(description = "法人名称")
    private String legalPerson;

    /** 法人证件号码 */
    @Schema(description = "法人证件号码")
    private String idNo;

    /** 法人联系方式 */
    @Schema(description = "联系方式")
    private String contact;
}
