package cn.daxpay.open.payment.device.terminal.dao;

import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminalChannelBind;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 系统终端-通道终端绑定管理
@Slf4j
@Repository
@RequiredArgsConstructor
public class TerminalChannelBindManager extends BaseManager<TerminalChannelBindMapper, DeviceTerminalChannelBind> {

    /// 是否已存在绑定
    public boolean existsBind(String systemTerminalNo, Long channelTerminalId) {
        return lambdaQuery()
                .eq(DeviceTerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .eq(DeviceTerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .exists();
    }

    /// 查询唯一绑定
    public Optional<DeviceTerminalChannelBind> findBind(String systemTerminalNo, Long channelTerminalId) {
        return lambdaQuery()
                .eq(DeviceTerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .eq(DeviceTerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .oneOpt();
    }

    /// 系统终端下的全部绑定
    public List<DeviceTerminalChannelBind> findBySystemTerminalNo(String systemTerminalNo) {
        return lambdaQuery()
                .eq(DeviceTerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .list();
    }

    /// 按多个系统终端号批量查询绑定关系(总览批量回填用, 避免 N+1)
    public List<DeviceTerminalChannelBind> findBySystemTerminalNos(List<String> systemTerminalNos) {
        if (CollUtil.isEmpty(systemTerminalNos)) {
            return List.of();
        }
        return lambdaQuery()
                .in(DeviceTerminalChannelBind::getSystemTerminalNo, systemTerminalNos)
                .list();
    }

    /// 支付终端解析: 系统终端号 + 通道商户号 定位绑定(冗余列单表查, 免 join 通道终端)
    public List<DeviceTerminalChannelBind> findBySystemTerminalNoAndChannelMchNo(String systemTerminalNo, String channelMchNo) {
        return lambdaQuery()
                .eq(DeviceTerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .eq(DeviceTerminalChannelBind::getChannelMchNo, channelMchNo)
                .list();
    }

    /// 通道终端下的全部绑定
    public List<DeviceTerminalChannelBind> findByChannelTerminalId(Long channelTerminalId) {
        return lambdaQuery()
                .eq(DeviceTerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .list();
    }

    /// 批量查询通道终端下的绑定(列表填充用)
    public List<DeviceTerminalChannelBind> findByChannelTerminalIds(List<Long> channelTerminalIds) {
        return lambdaQuery()
                .in(DeviceTerminalChannelBind::getChannelTerminalId, channelTerminalIds)
                .list();
    }

    /// 删除系统终端的全部绑定
    public void deleteBySystemTerminalNo(String systemTerminalNo) {
        lambdaUpdate()
                .eq(DeviceTerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .remove();
    }

    /// 删除通道终端的全部绑定
    public void deleteByChannelTerminalId(Long channelTerminalId) {
        lambdaUpdate()
                .eq(DeviceTerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .remove();
    }

    /// 删除单条绑定
    public void deleteBind(String systemTerminalNo, Long channelTerminalId) {
        lambdaUpdate()
                .eq(DeviceTerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .eq(DeviceTerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .remove();
    }
}
