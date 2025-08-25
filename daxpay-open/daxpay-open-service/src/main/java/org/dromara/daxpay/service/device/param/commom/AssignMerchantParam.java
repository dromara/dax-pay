package org.dromara.daxpay.service.device.param.commom;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 商户应用设备绑定参数
 * @author xxm
 * @since 2025/7/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户和应用码牌绑定参数")
public class AssignMerchantParam {

    @Schema(description = "代理商号")
    private String mchNo;

    @Schema(description = "应用号")
    private String appId;

    @NotEmpty(message = "设备ID列表不能为空")
    @Schema(description = "设备ID列表")
    private List<Long> ids;
}
