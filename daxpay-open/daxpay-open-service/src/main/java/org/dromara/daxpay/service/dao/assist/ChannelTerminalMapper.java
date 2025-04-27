package org.dromara.daxpay.service.dao.assist;

import org.dromara.daxpay.service.entity.assist.ChannelTerminal;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付终端设备通道上报记录
 * @author xxm
 * @since 2025/3/7
 */
@Mapper
public interface ChannelTerminalMapper extends MPJBaseMapper<ChannelTerminal> {
}
