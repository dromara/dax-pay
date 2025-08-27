package org.dromara.daxpay.service.merchant.param.gateway;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.AggregatePayTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关聚合支付配置参数
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合支付配置参数")
public class AggregatePayConfigParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 聚合支付类型
     * @see AggregatePayTypeEnum
     */
    @NotBlank(message = "聚合支付类型不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "聚合支付类型")
    private String aggregateType;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @NotBlank(message = "支付通道不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @NotBlank(message = "支付方式不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "支付方式")
    private String payMethod;

    /**
     * 其他支付方式
     */
    @Schema(description = "其他支付方式")
    private String otherMethod;

    /** 自动拉起支付 */
    @Schema(description = "自动拉起支付")
    @NotNull(message = "自动拉起支付不可为空")
    private Boolean autoLaunch;

    /** 需要获取OpenId */
    @Schema(description = "需要获取OpenId")
    @NotNull(message = "需要获取OpenId不可为空")
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

    /** 应用号 */
    @Schema(description = "应用号")
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    private String appId;
}
