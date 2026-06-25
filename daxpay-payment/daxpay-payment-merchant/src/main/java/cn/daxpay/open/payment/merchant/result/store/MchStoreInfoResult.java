package cn.daxpay.open.payment.merchant.result.store;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

/// # 门店信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "门店信息")
public class MchStoreInfoResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "门店号")
    private String storeNo;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "联系人电话")
    private String contactPhone;

    @Schema(description = "门店LOGO")
    private String logoUrl;

    @Schema(description = "门头照")
    private String facadeUrl;

    @Schema(description = "门店内景照")
    private String interiorUrl;

    @Schema(description = "行政区划代码")
    private String regionCode;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
