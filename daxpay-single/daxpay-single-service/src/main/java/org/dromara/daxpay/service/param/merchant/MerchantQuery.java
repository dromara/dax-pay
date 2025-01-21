package org.dromara.daxpay.service.param.merchant;

import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户参数
 * @author xxm
 * @since 2024/6/24
 */
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "商户参数")
public class MerchantQuery {

    /** 商户名称 */
    @Schema(description = "商户名称")
    private String mchName;

    /** 公司名称 */
    @Schema(description = "公司名称")
    private String companyName;

    /** 证件类型 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "证件类型")
    private String idType;

    /** 证件号 */
    @Schema(description = "证件号")
    private String idNo;

    /** 联系方式 */
    @Schema(description = "联系方式")
    private String tel;

    /** 法人名称 */
    @Schema(description = "法人名称")
    private String legalPerson;

    /** 法人证件号码 */
    @Schema(description = "法人证件号码")
    private String legalPersonIdNo;

    /** 是否有关联管理员 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否有关联管理员")
    private Boolean administrator;
}
