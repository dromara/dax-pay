package cn.daxpay.open.payment.device.terminal.dao;

import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminal;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 商户终端
@Mapper
public interface TerminalDeviceMapper extends MPJBaseMapper<DeviceTerminal> {
}
