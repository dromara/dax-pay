package org.dromara.daxpay.service.device.result.qrcode.config;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.core.enums.*;
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
@Schema(title = "码牌场景配置")
public class CashierCodeSceneConfigResult extends BaseResult {

    /** 码牌ID */
    @Schema(description = "主键ID")
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
