package org.dromara.daxpay.payment.merchant.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 小程序快捷支付配置
 * @author xxm
 * @since 2025/10/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷支付配置")
public class MiniQuicklyConfigResult {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 限制小程序支付方式 */
    @Schema(description = "限制小程序支付方式")
    private String limitPay;

    /** 小程序付款终端号 */
    @Schema(description = "小程序付款终端号")
    private String terminalNo;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
