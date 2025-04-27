package org.dromara.daxpay.service.dao.assist;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.entity.assist.ChannelTerminal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 支付终端设备通道上报记录
 * @author xxm
 * @since 2025/3/7
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelTerminalManager extends BaseManager<ChannelTerminalMapper, ChannelTerminal> {

    /**
     * 根据终端设备ID判断是否存在
     */
    public boolean existsByTerminalId(Long terminalId){
        return existedByField(ChannelTerminal::getTerminalId,terminalId);
    }

    /**
     * 根据终端设备ID和通道查询记录
     */
    public Optional<ChannelTerminal> findByTerminalIdAndChannel(Long terminalId, String channel){
        return lambdaQuery()
                .eq(ChannelTerminal::getTerminalId,terminalId)
                .eq(ChannelTerminal::getChannel,channel)
                .oneOpt();
    }

    /**
     * 根据终端设备ID和通道查询记录
     */
    public boolean existsByTerminal(Long terminalId, String channel, String terminalType){
        return lambdaQuery()
                .eq(ChannelTerminal::getTerminalId,terminalId)
                .eq(ChannelTerminal::getType,terminalType)
                .eq(ChannelTerminal::getChannel,channel)
                .exists();
    }

    /**
     * 根据终端号查询
     */
    public List<ChannelTerminal> findAllByTerminalId(Long terminalId){
        return lambdaQuery()
                .eq(ChannelTerminal::getTerminalId,terminalId)
                .list();

    }

    /**
     * 删除多余的终端设备通道上报记录
     * @param terminalId 终端设备ID
     * @param channelId 保留的通道ID
     * @param channel 通道
     * @param terminalType 终端类型
     */
    public void deleteByTerminalId(Long terminalId, Long channelId, String channel, String terminalType){
        lambdaUpdate()
                .eq(ChannelTerminal::getTerminalId,terminalId)
                .eq(ChannelTerminal::getChannel,channel)
                .eq(ChannelTerminal::getType,terminalType)
                .ne(ChannelTerminal::getId,channelId)
                .remove();
    }
}
