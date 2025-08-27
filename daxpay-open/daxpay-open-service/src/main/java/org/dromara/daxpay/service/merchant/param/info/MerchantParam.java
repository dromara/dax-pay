package org.dromara.daxpay.service.merchant.param.info;

import org.dromara.daxpay.core.enums.MerchantStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class MerchantParam {

    /** 主键ID */
    @Schema(description = "主键ID")
    @NotNull(message = "主键ID不可为空")
    private Long id;

    /** 商户名称 */
    @NotBlank(message = "商户名称不可为空")
    @Schema(description = "商户名称")
    private String mchName;

    /** 商户简称 */
    @NotBlank(message = "商户简称不可为空")
    @Schema(description = "商户简称")
    private String mchShortName;

    /**
     * 状态
     * @see MerchantStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /** 是否创建默认应用 */
    @Schema(description = "是否创建默认应用")
    private Boolean createDefaultApp;

    public Boolean getCreateDefaultApp() {
        return Boolean.TRUE.equals(createDefaultApp);
    }
}
