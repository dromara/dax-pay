package org.dromara.daxpay.service.device.service.terminal;

import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.enums.ChannelTerminalStatusEnum;
import org.dromara.daxpay.service.device.dao.terminal.ChannelTerminalManager;
import org.dromara.daxpay.service.device.dao.terminal.TerminalDeviceManager;
import org.dromara.daxpay.service.device.entity.terminal.ChannelTerminal;
import org.dromara.daxpay.service.device.param.terminal.ChannelGetAndCreateParam;
import org.dromara.daxpay.service.device.param.terminal.ChannelTerminalParam;
import org.dromara.daxpay.service.device.result.terminal.ChannelTerminalResult;
import org.dromara.daxpay.service.pay.dao.constant.TerminalConstManager;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.pay.strategy.AbsChannelTerminalStrategy;
import org.dromara.daxpay.service.pay.util.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通道终端设备管理
 * @author xxm
 * @since 2025/7/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelTerminalService {
    private final ChannelTerminalManager channelTerminalManager;
    private final TerminalDeviceManager terminalManager;
    private final PaymentAssistService paymentAssistService;
    private final TerminalConstManager terminalConstManager;

    /**
     * 根据终端ID查询通道报备列表
     */
    public List<ChannelTerminalResult> channelList(Long terminalId){
        var terminal = terminalManager.findById(terminalId).orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
        var terminalChannelMap = channelTerminalManager.findAllByTerminalId(terminalId).stream()
                .collect(Collectors.toMap(ChannelTerminal::genUpType, Function.identity(),  CollectorsFunction::retainFirst));
        var terminalTypes = terminalConstManager.findAllEnable();
        // 组装列表
        return terminalTypes.stream()
                .map(o -> {
                    var terminalChannel = terminalChannelMap.get(o.getChannel()+":"+o.getType());
                    if (Objects.isNull(terminalChannel)) {
                        return new ChannelTerminalResult()
                                .setChannel(o.getChannel())
                                .setType(o.getType())
                                .setTerminalId(terminal.getId())
                                .setTerminalNo(terminal.getTerminalNo());
                    } else {
                        return terminalChannel.toResult();
                    }
                })
                .toList();
    }

    /**
     * 通道报备
     */
    public void channelSubmit(Long terminalChannelId){
        var terminalChannel = channelTerminalManager.findById(terminalChannelId)
                .orElseThrow(() -> new DataNotExistException("通道支付终端报备信息不存在"));
        var terminal = terminalManager.findById(terminalChannel.getTerminalId())
                .orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
        // 查询通道报备策略
        var strategy = PaymentStrategyFactory.create(terminalChannel.getChannel(), AbsChannelTerminalStrategy.class);
        // 提交报备
        paymentAssistService.initMchAndApp(terminal.getMchNo(),terminal.getAppId());
        var terminalBo = strategy.submit(terminal,terminalChannel);
        terminalChannel.setOutTerminalNo(terminalBo.getDeviceNo())
                .setErrorMsg(terminalBo.getErrorMsg())
                .setStatus(terminalBo.isSuccess()? ChannelTerminalStatusEnum.SUBMIT.getCode(): ChannelTerminalStatusEnum.ERROR.getCode());
        channelTerminalManager.updateById(terminalChannel);

    }

    /**
     * 通道取消报备
     */
    public void channelCancel(Long terminalChannelId){
        var terminalChannel = channelTerminalManager.findById(terminalChannelId).orElseThrow(() -> new DataNotExistException("通道支付终端报备信息不存在"));
        var terminal = terminalManager.findById(terminalChannel.getTerminalId()).orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
        // 查询通道报备策略
        var strategy = PaymentStrategyFactory.create(terminalChannel.getChannel(), AbsChannelTerminalStrategy.class);
        // 注销设备
        paymentAssistService.initMchAndApp(terminal.getMchNo(),terminal.getAppId());
        var terminalBo = strategy.cancel(terminal, terminalChannel);
        terminalChannel.setErrorMsg(terminalBo.getErrorMsg())
                .setStatus(terminalBo.isSuccess()?ChannelTerminalStatusEnum.LOGGED.getCode(): ChannelTerminalStatusEnum.ERROR.getCode());
        channelTerminalManager.updateById(terminalChannel);
    }

    /**
     * 同步报备信息
     */
    public void channelSync(Long terminalChannelId){
        var terminalChannel = channelTerminalManager.findById(terminalChannelId).orElseThrow(() -> new DataNotExistException("通道支付终端报备信息不存在"));
        var terminal = terminalManager.findById(terminalChannel.getTerminalId()).orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
        // 查询通道报备策略
        var strategy = PaymentStrategyFactory.create(terminalChannel.getChannel(), AbsChannelTerminalStrategy.class);
        // 同步设备
        paymentAssistService.initMchAndApp(terminal.getMchNo(),terminal.getAppId());
        var terminalBo = strategy.sync(terminal, terminalChannel);
        terminalChannel.setErrorMsg(terminalBo.getErrorMsg())
                .setStatus(terminalBo.getStatus().getCode());
        // 成功则设置通道终端号
        if (terminalBo.getStatus() == ChannelTerminalStatusEnum.SUBMIT){
            terminalChannel.setOutTerminalNo(terminalBo.getDeviceNo());
        }
        channelTerminalManager.updateById(terminalChannel);
    }

    /**
     * 手动更改通道报备状态
     */
    public void channelEdit(ChannelTerminalParam param){
        var terminalChannel = channelTerminalManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("通道支付终端报备信息不存在"));
        terminalChannel.setStatus(param.getStatus())
                .setOutTerminalNo(param.getOutTerminalNo());
        if (param.isClearErrMsg()){
            terminalChannel.setErrorMsg(null);
        }
        channelTerminalManager.updateById(terminalChannel);
    }

    /**
     * 通道报备详情
     */
    public ChannelTerminalResult channelDetail(Long terminalChannelId){
        return channelTerminalManager.findById(terminalChannelId).map(ChannelTerminal::toResult)
                .orElseThrow(() -> new DataNotExistException("通道支付终端报备信息不存在"));
    }

    /**
     * 获取终端通道报备信息, 如果不存在进行创建
     */
    public ChannelTerminalResult getAndCreate(ChannelGetAndCreateParam param){
        var channelTerminal = channelTerminalManager.findByByTerminal(param.getTerminalId(), param.getChannel(), param.getTerminalType());
        if (channelTerminal.isEmpty()){
            var terminal = terminalManager.findById(param.getTerminalId()).orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
            var terminalChannel =  new ChannelTerminal()
                    .setTerminalId(param.getTerminalId())
                    .setTerminalNo(terminal.getTerminalNo())
                    .setChannel(param.getChannel())
                    .setType(param.getTerminalType())
                    .setStatus(ChannelTerminalStatusEnum.WAIT.getCode());
            terminalChannel.setAppId(terminal.getAppId())
                    .setMchNo(terminal.getMchNo());
            channelTerminalManager.save(terminalChannel);
            return terminalChannel.toResult();
        }
        return channelTerminal.get().toResult();
    }
}
