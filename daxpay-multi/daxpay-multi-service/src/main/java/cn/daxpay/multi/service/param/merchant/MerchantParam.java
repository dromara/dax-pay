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
    @Schema(title = "主键ID")
    @NotNull(message = "主键ID不可为空", groups = ValidationGroup.edit.class)
    private Long id;

    /** 商户名称 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(title = "商户名称")
    private String mchName;

    /** 公司名称 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(title = "公司名称")
    private String companyName;

    /** 证件类型 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(title = "证件类型")
    private String idType;

    /** 证件号 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(title = "证件号")
    private String idNo;

    /** 联系方式 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(title = "联系方式")
    private String tel;

    /** 法人名称 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(title = "法人名称")
    private String legalPerson;

    /** 法人证件号码 */
    @NotBlank(message = "商户名称不可为空", groups = ValidationGroup.add.class)
    @Schema(title = "法人证件号码")
    private String legalPersonIdNo;
}
