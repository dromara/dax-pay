package org.dromara.daxpay.service.isv.result.gateway;

import org.dromara.daxpay.core.enums.AggregatePayTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合支付配置")
public class IsvAggregatePayConfigResult extends MchResult {
    /**
     * 聚合支付类型
     * @see AggregatePayTypeEnum
     */
    @Schema(description = "聚合支付类型")
    private String aggregateType;

    /**
     * 通道
     * @see ChannelEnum
     */
    @Schema(description = "通道")
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

    /** 自动拉起支付 */
    @Schema(description = "自动拉起支付")
    private Boolean autoLaunch;

    /** 需要获取OpenId */
    @Schema(description = "需要获取OpenId")
    private Boolean needOpenId;

    /** OpenId获取方式 */
    @Schema(description = "OpenId获取方式")
    private String openIdGetType;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    @Schema(description = "发起调用的类型")
    private String callType;


}
