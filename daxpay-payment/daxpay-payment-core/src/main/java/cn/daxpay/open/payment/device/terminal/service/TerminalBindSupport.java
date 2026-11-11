package cn.daxpay.open.payment.device.terminal.service;

import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.entity.DeviceChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.DeviceTerminalChannelBind;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 商户终端与通道终端绑定规则
///
/// 建档即绑与手动绑定的关联约束(多入口共用同一套校验, 避免写出脏数据):
/// - 一条通道终端只归属一个商户终端;
/// - 同一商户终端在同一通道商户下按报送类型唯一(一个终端可分别做通用/微信/支付宝/银联报送)。
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalBindSupport {

    private final TerminalChannelBindManager terminalChannelBindManager;
    private final ChannelTerminalManager channelTerminalManager;

    /// 通道终端是否已被其它商户终端占用
    public boolean hasOtherOwner(Long channelTerminalId, String systemTerminalNo) {
        return terminalChannelBindManager.findByChannelTerminalId(channelTerminalId).stream()
                .anyMatch(bind -> !Objects.equals(bind.getSystemTerminalNo(), systemTerminalNo));
    }

    /// 取通道终端当前绑定的商户终端号(一条通道终端只归属一个商户终端, 遗留脏数据取首个)
    ///
    /// @param channelTerminalId 通道终端主键
    /// @return 绑定的商户终端号; 未绑定时返回 null
    public String findOwner(Long channelTerminalId) {
        return terminalChannelBindManager.findByChannelTerminalId(channelTerminalId).stream()
                .map(DeviceTerminalChannelBind::getSystemTerminalNo)
                .findFirst()
                .orElse(null);
    }

    /// 同一商户终端在同一通道商户、同一报送类型下是否已有其它通道终端
    ///
    /// @param excludeChannelTerminalId 排除的通道终端主键(校验自身时排除)
    public boolean hasTypeConflict(String systemTerminalNo, DeviceChannelTerminal target, Long excludeChannelTerminalId) {
        List<Long> boundIds = terminalChannelBindManager.findBySystemTerminalNo(systemTerminalNo).stream()
                .map(DeviceTerminalChannelBind::getChannelTerminalId)
                .filter(id -> !Objects.equals(id, excludeChannelTerminalId))
                .toList();
        if (boundIds.isEmpty()) {
            return false;
        }
        return channelTerminalManager.lambdaQuery()
                .in(DeviceChannelTerminal::getId, boundIds)
                .eq(DeviceChannelTerminal::getChannelMchNo, target.getChannelMchNo())
                .eq(DeviceChannelTerminal::getType, target.getType())
                .exists();
    }

    /// 组装绑定关系(冗余通道商户号/通道编码/报送类型, 唯一校验与支付解析均可单表完成)
    /// 绑定行写入方均为运营端, mchNo 在此显式赋值(运营端写 MchBaseEntity 必须显式 setMchNo)
    public DeviceTerminalChannelBind buildBind(String systemTerminalNo, DeviceChannelTerminal channel) {
        DeviceTerminalChannelBind bind = new DeviceTerminalChannelBind();
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        bind.setMchNo(channel.getMchNo());
        bind.setSystemTerminalNo(systemTerminalNo);
        bind.setChannelTerminalId(channel.getId());
        bind.setChannelMchNo(channel.getChannelMchNo());
        bind.setChannel(channel.getChannel());
        bind.setType(channel.getType());
        return bind;
    }
}
