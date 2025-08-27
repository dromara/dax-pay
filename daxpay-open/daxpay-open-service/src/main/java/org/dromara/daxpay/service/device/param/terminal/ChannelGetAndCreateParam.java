package org.dromara.daxpay.service.device.param.terminal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通道终端设备报备获取和自动创建参数
 * @author xxm
 * @since 2025/7/7
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道终端设备报备获取和自动创建参数")
public class ChannelGetAndCreateParam {

    @NotNull(message = "支付终端设备id不能为空")
    private Long terminalId;
    @NotBlank(message = "通道不能为空")
    private String channel;
    @NotBlank(message = "终端类型不能为空")
    private String terminalType;
}
