package cn.daxpay.open.payment.merchant.param.store;

import cn.daxpay.open.platform.capability.sensitiveword.validation.SensitiveWord;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// # 门店信息
///
@Data
@Accessors(chain = true)
@Schema(title = "门店信息")
public class MchStoreInfoParam {

    /// 主键
    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 门店名称
    @Schema(description = "门店名称")
    @NotBlank(message = "{validation.field.storeName.notBlank}")
    @SensitiveWord
    private String storeName;

    /// 联系人电话
    @Schema(description = "联系人电话")
    private String contactPhone;

    /// 门店LOGO
    @Schema(description = "门店LOGO")
    private String logoUrl;

    /// 门头照
    @Schema(description = "门头照")
    private String facadeUrl;

    /// 门店内景照
    @Schema(description = "门店内景照")
    private String interiorUrl;

    /// 行政区划代码
    @Schema(description = "行政区划代码")
    private String regionCode;

    /// 详细地址
    @Schema(description = "详细地址")
    private String address;

    /// 经度
    @Schema(description = "经度")
    private BigDecimal longitude;

    /// 纬度
    @Schema(description = "纬度")
    private BigDecimal latitude;

    /// 状态
    /// @see cn.daxpay.open.platform.core.enums.merchant.StoreStatusEnum
    @Schema(description = "状态")
    @NotBlank(message = "{validation.field.status.notBlank}")
    private String status;

    /// 是否默认门店(编辑时可改; true 时 Service 会 clear 同商户其它默认)
    @Schema(description = "是否默认门店")
    private boolean defaultStore;

    /// 备注
    @Schema(description = "备注")
    private String remark;
}
