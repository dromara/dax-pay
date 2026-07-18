package cn.daxpay.open.payment.device.terminal.dao;

import cn.daxpay.open.payment.device.terminal.entity.TerminalChannelBind;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 系统终端-通道终端绑定管理
@Slf4j
@Repository
@RequiredArgsConstructor
public class TerminalChannelBindManager extends BaseManager<TerminalChannelBindMapper, TerminalChannelBind> {

    /// 是否已存在绑定
    public boolean existsBind(String systemTerminalNo, Long channelTerminalId) {
        return lambdaQuery()
                .eq(TerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .eq(TerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .exists();
    }

    /// 查询唯一绑定
    public Optional<TerminalChannelBind> findBind(String systemTerminalNo, Long channelTerminalId) {
        return lambdaQuery()
                .eq(TerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .eq(TerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .oneOpt();
    }

    /// 系统终端下的全部绑定
    public List<TerminalChannelBind> findBySystemTerminalNo(String systemTerminalNo) {
        return lambdaQuery()
                .eq(TerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .list();
    }

    /// 通道终端下的全部绑定
    public List<TerminalChannelBind> findByChannelTerminalId(Long channelTerminalId) {
        return lambdaQuery()
                .eq(TerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .list();
    }

    /// 删除系统终端的全部绑定
    public void deleteBySystemTerminalNo(String systemTerminalNo) {
        lambdaUpdate()
                .eq(TerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .remove();
    }

    /// 删除通道终端的全部绑定
    public void deleteByChannelTerminalId(Long channelTerminalId) {
        lambdaUpdate()
                .eq(TerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .remove();
    }

    /// 删除单条绑定
    public void deleteBind(String systemTerminalNo, Long channelTerminalId) {
        lambdaUpdate()
                .eq(TerminalChannelBind::getSystemTerminalNo, systemTerminalNo)
                .eq(TerminalChannelBind::getChannelTerminalId, channelTerminalId)
                .remove();
    }
}
