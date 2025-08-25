package org.dromara.daxpay.service.device.dao.terminal;

import org.dromara.daxpay.service.device.entity.terminal.TerminalDevice;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付终端设备管理
 * @author xxm
 * @since 2025/3/7
 */
@Mapper
public interface TerminalDeviceMapper extends MPJBaseMapper<TerminalDevice> {
}
