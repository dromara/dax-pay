package org.dromara.daxpay.core.param.aggregate;

import org.dromara.daxpay.core.enums.AggregatePayTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关支付聚合支付参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关支付聚合支付参数")
public class AggregatePayParam {

    /** 支付订单号 */
    @NotBlank(message = "支付订单号不可为空")
    @Size(max = 32, message = "支付订单号不可超过32位")
    @Schema(description = "支付订单号")
    private String orderNo;

    /**
     * 聚合支付类型
     * @see AggregatePayTypeEnum
     */
    @NotBlank(message = "聚合支付类型不可为空")
    @Schema(description = "聚合支付类型")
    private String aggregateType;

    /** 用户唯一标识 */
    @Size(max = 1024, message = "唯一标识不可超过1024位")
    @Schema(description = "用户唯一标识")
    private String openId;

}
