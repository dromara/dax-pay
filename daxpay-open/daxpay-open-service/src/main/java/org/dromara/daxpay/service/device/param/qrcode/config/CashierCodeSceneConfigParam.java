package org.dromara.daxpay.service.device.param.qrcode.config;

import org.dromara.daxpay.core.enums.CashierSceneEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 特定类型码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "码牌支付场景配置")
public class CashierCodeSceneConfigParam {

    /** 主键 */
    @Schema(description = "主健")
    private Long id;

    /** 配置ID */
    @Schema(description = "配置ID")
    private Long configId;

    /**
     * 收银场景
     * @see CashierSceneEnum
     */
    @Schema(description = "收银场景")
    private String scene;

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

    /** OpenId获取方式 */
    @Schema(description = "OpenId获取方式")
    private String openIdGetType;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    @Schema(description = "发起调用的类型")
    private String callType;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
