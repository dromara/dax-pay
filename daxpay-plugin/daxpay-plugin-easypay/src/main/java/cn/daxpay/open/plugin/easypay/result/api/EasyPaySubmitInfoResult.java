package cn.daxpay.open.plugin.easypay.result.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Schema(title = "易支付收银台信息(内部)")
public class EasyPaySubmitInfoResult {
    private Long id;
    private String type;
    private String name;
    private BigDecimal money;
    private String payUrl;
    private String payBody;
    private String pcCallType;
    private Integer status;
    private String returnUrl;
}
