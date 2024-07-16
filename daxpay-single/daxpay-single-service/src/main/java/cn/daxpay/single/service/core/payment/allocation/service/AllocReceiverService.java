package cn.daxpay.single.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.core.code.AllocReceiverTypeEnum;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.exception.DataErrorException;
import cn.daxpay.single.core.exception.OperationFailException;
import cn.daxpay.single.core.exception.OperationProcessingException;
import cn.daxpay.single.core.param.payment.allocation.AllocReceiverAddParam;
import cn.daxpay.single.core.param.payment.allocation.AllocReceiverRemoveParam;
import cn.daxpay.single.core.param.payment.allocation.QueryAllocReceiverParam;
import cn.daxpay.single.core.result.allocation.AllocReceiverAddResult;
import cn.daxpay.single.core.result.allocation.AllocReceiverRemoveResult;
import cn.daxpay.single.core.result.allocation.AllocReceiverResult;
import cn.daxpay.single.core.result.allocation.AllocReceiversResult;
import cn.daxpay.single.service.core.payment.allocation.convert.AllocReceiverConvert;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocGroupReceiverManager;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocReceiverManager;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocReceiver;
import cn.daxpay.single.service.dto.allocation.AllocReceiverDto;
import cn.daxpay.single.service.func.AbsAllocReceiverStrategy;
import cn.daxpay.single.service.param.allocation.receiver.AllocReceiverQuery;
import cn.daxpay.single.service.util.PayStrategyFactory;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分账接收方服务类
 * @author xxm
 * @since 2024/3/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocReceiverService {

    private final AllocReceiverManager manager;

    private final AllocGroupReceiverManager groupReceiverManager;

    private final AllocReceiverManager allocReceiverManager;

    private final LockTemplate lockTemplate;

    /**
     * 分页
     */
    public PageResult<AllocReceiverDto> page(PageParam pageParam, AllocReceiverQuery query){
        return MpUtil.convert2DtoPageResult(manager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public AllocReceiverDto findById(Long id){
        return manager.findById(id).map(AllocReceiver::toDto).orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
    }

    /**
     * 编码是否存在
     */
    public boolean existsByReceiverNo(String receiverNo){
        return manager.existedByReceiverNo(receiverNo);
    }

    /**
     * 获取可以分账的通道
     */
    public List<LabelValue> findChannels(){
        return Arrays.asList(
                new LabelValue(PayChannelEnum.ALI.getName(),PayChannelEnum.ALI.getCode()),
                new LabelValue(PayChannelEnum.WECHAT.getName(),PayChannelEnum.WECHAT.getCode())
        );
    }

    /**
     * 根据通道获取分账接收方类型
     */
    public List<LabelValue> findReceiverTypeByChannel(String channel){
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        List<AllocReceiverTypeEnum> list;
        if (channelEnum == PayChannelEnum.ALI){
            list = AllocReceiverTypeEnum.ALI_LIST;
        } else if (channelEnum == PayChannelEnum.WECHAT){
            list = AllocReceiverTypeEnum.WECHAT_LIST;
        } else {
            throw new BizException("非法的分账通道类型");
        }
        return list.stream()
                .map(item -> new LabelValue(item.getName(),item.getCode()))
                .collect(Collectors.toList());
    }

    /**
     * 添加分账接收方并同步到三方支付系统中
     */
    public AllocReceiverAddResult addAndSync(AllocReceiverAddParam param){
        // 判断是否已经添加
        LockInfo lock = lockTemplate.lock("payment:receiver:" + param.getReceiverNo(),10000,200);
        if (Objects.isNull(lock)){
            throw new OperationProcessingException("分账方处理中，请勿重复操作");
        }
        try {
            Optional<AllocReceiver> receiverOptional = allocReceiverManager.findByReceiverNo(param.getReceiverNo());
            if (receiverOptional.isPresent()){
                throw new OperationFailException("该接收方已存在");
            }
            AllocReceiver receiver = AllocReceiverConvert.CONVERT.convert(param);
            // 获取策略
            AbsAllocReceiverStrategy receiverStrategy = PayStrategyFactory.create(param.getChannel(), AbsAllocReceiverStrategy.class);
            // 校验
            receiverStrategy.setAllocReceiver(receiver);
            if (!receiverStrategy.validation()){
                throw new ValidationFailedException("接收方信息校验失败");
            }
            // 先添加到三方支付系统中, 然后保存到本地
            receiverStrategy.doBeforeHandler();
            receiverStrategy.bind();
            manager.save(receiver);
        } finally {
            lockTemplate.releaseLock(lock);
        }
        return new AllocReceiverAddResult();
    }

    /**
     * 接口删除
     */
    public AllocReceiverRemoveResult remove(AllocReceiverRemoveParam param){
        // 判断是否存在
        AllocReceiver receiver = allocReceiverManager.findByReceiverNo(param.getReceiverNo())
                .orElseThrow(() -> new DataErrorException("该接收方不存在"));
        if (groupReceiverManager.isUsed(receiver.getId())){
            throw new OperationFailException("该接收方已被使用，无法被删除");
        }
        // 获取策略
        AbsAllocReceiverStrategy receiverStrategy = PayStrategyFactory.create(receiver.getChannel(), AbsAllocReceiverStrategy.class);
        LockInfo lock = lockTemplate.lock("payment:receiver:" + param.getReceiverNo(),10000,200);
        if (Objects.isNull(lock)){
            throw new OperationProcessingException("分账方处理中，请勿重复操作");
        }
        try {
            // 校验
            receiverStrategy.setAllocReceiver(receiver);
            receiverStrategy.validation();
            receiverStrategy.doBeforeHandler();
            receiverStrategy.unbind();
            manager.deleteById(receiver.getId());
        } finally {
            lockTemplate.releaseLock(lock);
        }
        return new AllocReceiverRemoveResult();
    }

    /**
     * 接口查询
     */
    public AllocReceiversResult queryAllocReceive(QueryAllocReceiverParam param){
        // 查询对应通道分账接收方的信息
        List<AllocReceiver> list = manager.lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getChannel()), AllocReceiver::getChannel, param.getChannel())
                .eq(StrUtil.isNotBlank(param.getReceiverNo()), AllocReceiver::getReceiverNo, param.getReceiverNo())
                .list();
        List<AllocReceiverResult> receivers = list.stream()
                .map(AllocReceiverConvert.CONVERT::toResult)
                .collect(Collectors.toList());
        return new AllocReceiversResult().setReceivers(receivers);
    }
}
