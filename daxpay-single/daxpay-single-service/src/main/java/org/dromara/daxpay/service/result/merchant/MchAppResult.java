package org.dromara.daxpay.service.result.merchant;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.core.enums.MerchantNotifyTypeEnum;
import org.dromara.daxpay.core.enums.SignTypeEnum;
import org.dromara.daxpay.service.enums.MchAppStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 商户应用
 * @author xxm
 * @since 2024/6/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户应用")
public final class MchAppResult extends BaseResult {

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;

    /** 应用名称 */
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 签名方式
     * @see SignTypeEnum
     */
    @Schema(description = "签名方式")
    private String signType;

    /** 签名秘钥 */
    @Schema(description = "签名秘钥")
    private String signSecret;

    /** 是否对请求进行验签 */
    @Schema(description = "是否对请求进行验签")
    private boolean reqSign;

    /** 支付限额 */
    @Schema(description = "支付限额")
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    @Schema(description = "订单默认超时时间(分钟)")
    private Integer orderTimeout;

    /**
     * 状态
     * @see MchAppStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see MerchantNotifyTypeEnum
     */
    @Schema(description = "异步消息通知类型")
    private String notifyType;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @Schema(description = "通知地址")
    private String notifyUrl;

}
