package cn.bootx.platform.daxpay.dto.merchant;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户
 * @author xxm
 * @date 2023-05-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "商户")
@Accessors(chain = true)
public class MerchantInfoDto extends BaseDto {

    @Schema(description = "商户号")
    private String mchNo;
    @Schema(description = "商户名称")
    private String mchName;
    @Schema(description = "商户简称")
    private String mchShortName;
    @Schema(description = "类型")
    private String type;
    @Schema(description = "联系人姓名")
    private String contactName;
    @Schema(description = "联系人手机号")
    private String contactTel;
    @Schema(description = "状态类型")
    private String state;
    @Schema(description = "商户备注")
    private String remark;

}
