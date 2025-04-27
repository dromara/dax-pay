package org.dromara.daxpay.service.result.gateway.config;

import org.dromara.daxpay.core.enums.*;
import org.dromara.daxpay.service.result.merchant.MchAppResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 码牌配置明细
 * @author xxm
 * @since 2024/11/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "特定类型码牌配置")
public class CashierCodeItemConfigResult extends MchAppResult {

    /** 码牌ID */
    @Schema(description = "主键ID")
    private Long codeId;

    /**
     * 码牌类型
     * @see CashierCodeTypeEnum
     */
    @Schema(description = "收银台类型")
    private String type;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "支付方式")
    private String payMethod;

    /**
     * 其他支付方式
     */
    @Schema(description = "其他支付方式")
    private String otherMethod;

    /** 需要获取OpenId */
    @Schema(description = "需要获取OpenId")
    private Boolean needOpenId;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    @Schema(description = "发起调用的类型")
    private String callType;

    /**
     * 限制用户支付类型, 目前支持限制信用卡
     * @see PayLimitPayEnum
     */
    @Schema(description = "限制用户支付类型")
    private String limitPay;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
