package cn.daxpay.open.plugin.easypay.param.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title = "易支付H5支付参数")
public class EasyPayH5PayParam {
    @NotNull
    private Long id;
    private String openId;
    private String scene;
}
