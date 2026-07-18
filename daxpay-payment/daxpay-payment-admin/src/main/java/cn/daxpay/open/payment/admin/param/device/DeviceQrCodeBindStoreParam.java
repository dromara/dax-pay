package cn.daxpay.open.payment.admin.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 码牌绑定门店参数
@Data
@Accessors(chain = true)
@Schema(title = "码牌绑定门店参数")
public class DeviceQrCodeBindStoreParam {

    /// 码牌主键列表
    @Schema(description = "码牌主键列表")
    @NotEmpty(message = "{validation.field.ids.notEmpty}")
    private List<Long> ids;

    /// 门店号(对应 mch_store_info.store_no)
    @Schema(description = "门店号")
    @NotBlank(message = "{validation.field.storeNo.notBlank}")
    @Size(max = 64, message = "{validation.field.storeNo.size}")
    private String storeNo;
}
