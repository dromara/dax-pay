package org.dromara.daxpay.service.service.assist;

import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.core.enums.ChannelTerminalStatusEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.assist.ChannelTerminalManager;
import org.dromara.daxpay.service.dao.assist.TerminalConstManager;
import org.dromara.daxpay.service.dao.assist.TerminalDeviceManager;
import org.dromara.daxpay.service.entity.assist.ChannelTerminal;
import org.dromara.daxpay.service.entity.assist.TerminalDevice;
import org.dromara.daxpay.service.param.termina.ChannelTerminalParam;
import org.dromara.daxpay.service.param.termina.TerminalDeviceParam;
import org.dromara.daxpay.service.param.termina.TerminalDeviceQuery;
import org.dromara.daxpay.service.result.termina.ChannelTerminalResult;
import org.dromara.daxpay.service.result.termina.TerminalDeviceResult;
import org.dromara.daxpay.service.strategy.AbsChannelTerminalStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 支付终端设备管理
 * @author xxm
 * @since 2025/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalDeviceService {
    private final TerminalDeviceManager terminalManager;
    private final ChannelTerminalManager channelTerminalManager;
    private final TerminalConstManager terminalConstManager;
    private final PaymentAssistService paymentAssistService;

    /**
     * 分页
     */
    public PageResult<TerminalDeviceResult> page(PageParam pageParam, TerminalDeviceQuery query){
        return MpUtil.toPageResult(terminalManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public TerminalDeviceResult findById(Long id){
        return terminalManager.findById(id).map(TerminalDevice::toResult)
                .orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
    }

    /**
     * 添加
     */
    public void add(TerminalDeviceParam param){
        paymentAssistService.initMchAndApp(param.getAppId());
        var mchApp = PaymentContextLocal.get().getMchAppInfo();
        String uuid = UUID.fastUUID().toString(true);
        TerminalDevice entity = TerminalDevice.init(param);
        entity.setTerminalNo(uuid)
                .setAppId(mchApp.getAppId())
                .setMchNo(mchApp.getMchNo());
        terminalManager.save(entity);
    }

    /**
     * 修改
     */
    public void edit(TerminalDeviceParam param){
        var entity = terminalManager.findById(param.getId()).orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
        BeanUtil.copyProperties(param, entity, CopyOptions.create().ignoreNullValue());
        terminalManager.updateById(entity);
    }

    /**
     * 删除
     */
    public void delete(Long id){
        // 判断是有已经进行了上报的
        if (channelTerminalManager.existsByTerminalId(id)){
            throw new DataErrorException("该终端设备下有通道报备记录，无法被删除");
        }
        terminalManager.deleteById(id);
    }

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
                .setStatus(terminalBo.isSuccess()?ChannelTerminalStatusEnum.SUBMIT.getCode(): ChannelTerminalStatusEnum.ERROR.getCode());
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
     * 生成通道报备记录
     */
    public void channelAdd(Long terminalId, String channel, String terminalType){
        if (channelTerminalManager.existsByTerminal(terminalId, channel,terminalType)){
            throw new DataErrorException("该通道关联支付设备已存在，无法进行创建");
        }
        var terminal = terminalManager.findById(terminalId).orElseThrow(() -> new DataNotExistException("支付终端设备不存在"));
        this.channelAdd(terminal, channel, terminalType);
    }

    /**
     * 数据检查修复
     */
    public void checkAndRepair(Long terminalId){
        // 查询通道报备列表
        var terminalChannels = channelTerminalManager.findAllByTerminalId(terminalId);
        // 判断是否有重复的, 进行删除
        var terminalChannelGroup = terminalChannels.stream()
                .collect(Collectors.groupingBy(ChannelTerminal::genUpType));
        terminalChannelGroup.forEach((key, value) -> {
            if (value.size() > 1){
                var terminalChannel = value.getFirst();
                channelTerminalManager.deleteByTerminalId(terminalId, terminalChannel.getId(), terminalChannel.getChannel(), terminalChannel.getType());
            }
        });
    }

    /**
     * 生成通道报备记录
     */
    private void channelAdd(TerminalDevice terminal, String channel,String terminalType){
        var terminalChannel =  new ChannelTerminal()
                .setTerminalId(terminal.getId())
                .setTerminalNo(terminal.getTerminalNo())
                .setChannel(channel)
                .setType(terminalType)
                .setStatus(ChannelTerminalStatusEnum.WAIT.getCode());
        terminalChannel.setAppId(terminal.getAppId())
                .setMchNo(terminal.getMchNo());
        channelTerminalManager.save(terminalChannel);
    }

    /**
     * 终端下拉列表
     */
    public List<LabelValue> dropdown(String appId) {
        return terminalManager.findAllByField(MchAppBaseEntity::getAppId, appId).stream()
                .map(o -> new LabelValue(o.getName(), o.getTerminalNo()))
                .toList();
    }
}
