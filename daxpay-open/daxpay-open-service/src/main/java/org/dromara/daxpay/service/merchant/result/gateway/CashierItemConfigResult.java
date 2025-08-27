package org.dromara.daxpay.service.merchant.result.gateway;

import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.GatewayPayEnvTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2024/11/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "收银台配置项")
public class CashierItemConfigResult extends MchResult {
    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    @Schema(description = "发起调用的类型")
    private String callType;

    /** 是否推荐 */
    @Schema(description = "是否推荐")
    private Boolean recommend;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 图标 */
    @Schema(description = "图标")
    private String icon;

    /**
     * 生效的支付环境列表
     * @see GatewayPayEnvTypeEnum
     */
    @Schema(description = "生效的支付环境列表")
    private List<String> envTypes;

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

    /** 排序 */
    @Schema(description = "排序")
    private Double sortNo;
}
