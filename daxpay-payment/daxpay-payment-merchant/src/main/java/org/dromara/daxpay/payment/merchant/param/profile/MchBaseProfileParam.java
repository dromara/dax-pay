package org.dromara.daxpay.payment.merchant.param.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户基础资料参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户基础资料参数")
public class MchBaseProfileParam {

    /// 商户号
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    /// 联系人姓名
    @Schema(description = "联系人姓名")
    private String contactName;

    /// 联系电话
    @Schema(description = "联系电话")
    private String contactPhone;

    /// 联系邮箱
    @Schema(description = "联系邮箱")
    private String contactEmail;

    /// 省份编码
    @Schema(description = "省份编码")
    private String provinceCode;

    /// 城市编码
    @Schema(description = "城市编码")
    private String cityCode;

    /// 详细地址
    @Schema(description = "详细地址")
    private String address;

    /// 备注
    @Schema(description = "备注")
    private String remark;
}
