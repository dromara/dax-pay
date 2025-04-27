package org.dromara.daxpay.service.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 网关收银台订单信息
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关收银台订单信息")
public class GatewayOrderResult {

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 标题 */
    @Schema(description = "标题")
    private String title;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 金额(元) */
    @Schema(description = "金额(元)")
    private BigDecimal amount;

    /** 超时时间 */
    @Schema(description = "超时时间")
    private LocalDateTime expiredTime;
}
