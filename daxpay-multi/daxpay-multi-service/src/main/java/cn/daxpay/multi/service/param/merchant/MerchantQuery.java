package cn.daxpay.multi.service.param.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class MerchantQuery {


    /** 商户名称 */
    @Schema(title = "商户名称")
    private String mchName;

    /** 公司名称 */
    @Schema(title = "公司名称")
    private String companyName;

    /** 证件类型 */
    @Schema(title = "证件类型")
    private String idType;

    /** 证件号 */
    @Schema(title = "证件号")
    private String idNo;

    /** 联系方式 */
    @Schema(title = "联系方式")
    private String tel;

    /** 法人名称 */
    @Schema(title = "法人名称")
    private String legalPerson;

    /** 法人证件号码 */
    @Schema(title = "法人证件号码")
    private String legalPersonIdNo;
}
