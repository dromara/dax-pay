package cn.daxpay.open.payment.device.terminal.service;

import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.entity.DeviceChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/// # 商户终端关联通道终端聚合填充
///
/// 运营端商户终端总览([cn.daxpay.open.payment.admin.service.device.TerminalDeviceAdminService#overviewPage])
/// 与商户端商户终端分页([cn.daxpay.open.payment.merchant.service.device.MchTerminalDeviceService#page])
/// 共用: 一次查绑定、一次查通道终端, 按商户终端号分组回填, 不产生 N+1 查询。
///
/// 绑定指向的通道终端已删除或不存在时跳过该条; 结果按通道终端主键(即建档顺序)稳定排序。
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalChannelFillSupport {

    private final TerminalChannelBindManager terminalChannelBindManager;
    private final ChannelTerminalManager channelTerminalManager;

    /// 批量填充商户终端关联的通道终端(无绑定置空列表, 保证前端拿到空数组而非 null)
    public void fill(List<TerminalDeviceResult> results) {
        if (CollUtil.isEmpty(results)) {
            return;
        }
        List<String> terminalNos = results.stream()
                .map(TerminalDeviceResult::getTerminalNo)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .toList();
        if (terminalNos.isEmpty()) {
            return;
        }
        List<DeviceTerminalChannelBind> binds = terminalChannelBindManager.findBySystemTerminalNos(terminalNos);
        Map<String, List<DeviceTerminalChannelBind>> bindGroup = binds.stream()
                .collect(Collectors.groupingBy(DeviceTerminalChannelBind::getSystemTerminalNo));
        Map<Long, ChannelTerminalResult> channelTerminalMap = this.loadChannelTerminals(binds);
        for (TerminalDeviceResult result : results) {
            List<Long> channelTerminalIds = bindGroup.getOrDefault(result.getTerminalNo(), List.of()).stream()
                    .map(DeviceTerminalChannelBind::getChannelTerminalId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .toList();
            List<ChannelTerminalResult> channelTerminals = channelTerminalIds.stream()
                    .map(channelTerminalMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            result.setChannelTerminals(channelTerminals);
        }
    }

    /// 批量加载本次涉及的全部通道终端并按主键索引(逻辑删除的数据自动过滤, 调用方按需跳过)
    private Map<Long, ChannelTerminalResult> loadChannelTerminals(List<DeviceTerminalChannelBind> binds) {
        List<Long> channelTerminalIds = binds.stream()
                .map(DeviceTerminalChannelBind::getChannelTerminalId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (channelTerminalIds.isEmpty()) {
            return Map.of();
        }
        return channelTerminalManager.findAllByIds(channelTerminalIds).stream()
                .map(DeviceChannelTerminal::toResult)
                .collect(Collectors.toMap(ChannelTerminalResult::getId, result -> result, (a, b) -> a));
    }
}
