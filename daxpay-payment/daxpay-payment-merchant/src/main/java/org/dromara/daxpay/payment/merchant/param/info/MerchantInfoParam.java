package org.dromara.daxpay.payment.merchant.param.info;

import org.dromara.daxpay.platform.core.enums.merchant.MerchantStatusEnum;
import org.dromara.daxpay.platform.core.enums.subject.SubjectTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户参数")
public class MerchantInfoParam {

    /// 主键ID
    @Schema(description = "主键ID")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    /// 商户名称
    @NotBlank(message = "{validation.field.mchName.notBlank}")
    @Schema(description = "商户名称")
    private String mchName;

    /// 商户简称
    @NotBlank(message = "{validation.field.mchShortName.notBlank}")
    @Schema(description = "商户简称")
    private String mchShortName;

    /// 主体类型
    /// @see SubjectTypeEnum
    @Schema(description = "主体类型")
    private String subjectType;

    /// 状态
    /// @see MerchantStatusEnum
    @Schema(description = "状态")
    private String status;

    /// 服务商号
    @Schema(description = "服务商号")
    private String isvNo;

    /// 是否创建默认应用
    @Schema(description = "是否创建默认应用")
    private Boolean createDefaultApp;

    public Boolean getCreateDefaultApp() {
        return Boolean.TRUE.equals(createDefaultApp);
    }
}

