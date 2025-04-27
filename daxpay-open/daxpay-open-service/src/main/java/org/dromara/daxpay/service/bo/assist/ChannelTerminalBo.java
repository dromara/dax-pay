package org.dromara.daxpay.service.bo.assist;

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
     * 错误提示
     */
    private String errorMsg;
}
