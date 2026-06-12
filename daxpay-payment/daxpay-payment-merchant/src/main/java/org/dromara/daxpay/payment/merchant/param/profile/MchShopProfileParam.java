package org.dromara.daxpay.payment.merchant.param.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 商户经营场所信息参数
///

@Data
@Accessors(chain = true)
public class MchShopProfileParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "门店类型")
    private String type;

    @Schema(description = "结算类型")
    private String settleType;

    @Schema(description = "经营场所名称")
    private String name;

    @Schema(description = "省市区编码")
    private List<String> regionCode;

    @Schema(description = "经营场所详细地址")
    private String address;

    @Schema(description = "门头照")
    private String doorPic;

    @Schema(description = "室内照")
    private String insidePic;

    @Schema(description = "收银台照片")
    private String cashierPic;
}
