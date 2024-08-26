package cn.daxpay.multi.service.result.merchant;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户信息
 * @author xxm
 * @since 2024/6/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户信息")
public class MerchantResult extends BaseResult {

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 商户名称 */
    @Schema(description = "商户名称")
    private String mchName;

    /** 公司名称 */
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

    /** 是否有关联管理员 */
    @Schema(description = "是否有关联管理员")
    private Boolean administrator;

    /** 关联管理员用户 */
    @Schema(description = "关联管理员用户")
    private Long adminUserId;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

}
