package cn.bootx.platform.daxpay.param.assist;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 预付订单再次签名参数
 * @author xxm
 * @since 2024/2/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "预付订单再次签名")
public class WxJsapiPrePayParam extends PaymentCommonParam {

    @Schema(description = "预付订单ID")
    private String prepayId;


}
