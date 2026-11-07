package cn.daxpay.open.payment.device.terminal.service;

import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.TerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/// # 通道终端台账查询(多端共用)
///
/// 运营端与商户端的通道终端分页/详情共用同一套查询、字典翻译与已绑定系统终端回填,
/// 避免两端各自拼装导致字段口径漂移。商户号隔离由调用方在查询条件上写入, 本类不做商户过滤。
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelTerminalQuerySupport {

    private final ChannelTerminalManager channelTerminalManager;
    private final TerminalChannelBindManager terminalChannelBindManager;
    private final TerminalDeviceManager terminalDeviceManager;
    private final TransService transService;

    /// 分页查询(含字典翻译与已绑定系统终端回填)
    public PageResult<ChannelTerminalResult> page(PageParam pageParam, ChannelTerminalQuery query) {
        PageResult<ChannelTerminalResult> pageResult = MpUtil.toPageResult(channelTerminalManager.page(pageParam, query));
        fillSystemTerminals(pageResult.getRecords());
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询(含字典翻译与已绑定系统终端回填)
    public ChannelTerminalResult findById(Long id) {
        ChannelTerminalResult result = channelTerminalManager.findById(id)
                .map(ChannelTerminal::toResult)
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        fillSystemTerminals(List.of(result));
        transService.translate(result);
        return result;
    }

    /// 批量填充通道终端已绑定的系统终端列表(一条通道终端只归属一个系统终端, 列表形态兼容遗留脏数据)
    public void fillSystemTerminals(List<ChannelTerminalResult> results) {
        if (results.isEmpty()) {
            return;
        }
        List<Long> ids = results.stream()
                .map(ChannelTerminalResult::getId)
                .filter(Objects::nonNull)
                .toList();
        if (ids.isEmpty()) {
            return;
        }
        // 按通道终端分组绑定关系
        Map<Long, List<TerminalChannelBind>> bindGroup = terminalChannelBindManager.findByChannelTerminalIds(ids).stream()
                .collect(Collectors.groupingBy(TerminalChannelBind::getChannelTerminalId));
        if (bindGroup.isEmpty()) {
            return;
        }
        // 批量查系统终端并按编码索引
        List<String> terminalNos = bindGroup.values().stream()
                .flatMap(List::stream)
                .map(TerminalChannelBind::getSystemTerminalNo)
                .distinct()
                .toList();
        Map<String, TerminalDeviceResult> deviceMap = terminalDeviceManager.lambdaQuery()
                .in(TerminalDevice::getTerminalNo, terminalNos)
                .list()
                .stream()
                .map(TerminalDevice::toResult)
                .collect(Collectors.toMap(TerminalDeviceResult::getTerminalNo, r -> r, (a, b) -> a));
        for (ChannelTerminalResult result : results) {
            List<TerminalChannelBind> binds = bindGroup.get(result.getId());
            if (Objects.isNull(binds)) {
                continue;
            }
            List<TerminalDeviceResult> systemTerminals = binds.stream()
                    .map(TerminalChannelBind::getSystemTerminalNo)
                    .map(deviceMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            result.setSystemTerminals(systemTerminals);
        }
    }
}
