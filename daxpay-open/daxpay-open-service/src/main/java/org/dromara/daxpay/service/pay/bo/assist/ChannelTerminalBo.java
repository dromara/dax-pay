package org.dromara.daxpay.service.pay.bo.assist;

import org.dromara.daxpay.core.enums.ChannelTerminalStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通道终端设备报送记录结果
 * @author xxm
 * @since 2025/3/10
 */
@Data
@Accessors(chain = true)
public class ChannelTerminalBo {

    /**
     * 通道返回的设备注册号
     */
    private String deviceNo;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 报备状态
     */
    private ChannelTerminalStatusEnum status;

    /**
     * 错误提示
     */
    private String errorMsg;
}
