package org.dromara.daxpay.payment.merchant.result.profile;

import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/// # 商户经营场所信息结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MchShopProfileResult extends MchResult {

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
