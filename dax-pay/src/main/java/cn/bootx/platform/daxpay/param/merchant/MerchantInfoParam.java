package cn.bootx.platform.daxpay.param.merchant;

import cn.bootx.platform.common.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import static cn.bootx.platform.common.core.annotation.QueryParam.CompareTypeEnum.*;

/**
 * 商户
 *
 * @author xxm
 * @date 2023-05-17
 */
@Data
@Schema(title = "商户")
@Accessors(chain = true)
public class MerchantInfoParam {

    @Schema(description = "主键")
    private Long id;

    @QueryParam(type = LIKE)
    @Schema(description = "商户号")
    private String mchNo;

    @QueryParam(type = LIKE)
    @Schema(description = "商户名称")
    private String mchName;

    @QueryParam(type = LIKE)
    @Schema(description = "商户简称")
    private String mchShortName;

    @Schema(description = "类型")
    private String type;

    @QueryParam(type = LIKE)
    @Schema(description = "联系人姓名")
    private String contactName;

    @QueryParam(type = LIKE)
    @Schema(description = "联系人手机号")
    private String contactTel;

    @Schema(description = "状态类型")
    private String state;

    @Schema(description = "商户备注")
    private String remark;

}
