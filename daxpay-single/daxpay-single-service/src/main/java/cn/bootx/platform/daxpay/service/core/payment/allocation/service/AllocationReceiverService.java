package cn.bootx.platform.daxpay.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.core.payment.allocation.convert.AllocationReceiverConvert;
import cn.bootx.platform.daxpay.service.core.payment.allocation.dao.AllocationGroupReceiverManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.dao.AllocationReceiverManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import cn.bootx.platform.daxpay.service.core.payment.allocation.factory.AllocationReceiverFactory;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationReceiverDto;
import cn.bootx.platform.daxpay.service.func.AbsAllocationReceiverStrategy;
import cn.bootx.platform.daxpay.service.param.allocation.group.AllocationReceiverParam;
import cn.bootx.platform.daxpay.service.param.allocation.group.AllocationReceiverQuery;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分账接收方服务类
 * @author xxm
 * @since 2024/3/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationReceiverService {

    private final AllocationReceiverManager manager;

    private final AllocationGroupReceiverManager groupReceiverManager;

    /**
     * 分页
     */
    public PageResult<AllocationReceiverDto> page(PageParam pageParam, AllocationReceiverQuery query){
        return MpUtil.convert2DtoPageResult(manager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public AllocationReceiverDto findById(Long id){
        return manager.findById(id).map(AllocationReceiver::toDto).orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
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
        List<AllocationReceiverTypeEnum> list;
        if (channelEnum == PayChannelEnum.ALI){
            list = AllocationReceiverTypeEnum.ALI_LIST;
        } else if (channelEnum == PayChannelEnum.WECHAT){
            list = AllocationReceiverTypeEnum.WECHAT_LIST;
        } else {
            throw new BizException("非法的分账通道类型");
        }
        return list.stream()
                .map(item -> new LabelValue(item.getName(),item.getCode()))
                .collect(Collectors.toList());
    }

    /**
     * 添加分账接收方
     */
    public void add(AllocationReceiverParam param){
        // 首先添加网关的分账接收方
        AllocationReceiver receiver = AllocationReceiverConvert.CONVERT.convert(param);

        // 获取策略
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(param.getChannel());
        AbsAllocationReceiverStrategy receiverStrategy = AllocationReceiverFactory.create(channelEnum);
        // 校验
        receiverStrategy.setAllocationReceiver(receiver);
        if (!receiverStrategy.validation()){
            throw new BizException("接收方信息校验失败");
        }

        receiver.setSync(false);
        manager.save(receiver);
    }

    /**
     * 修改信息
     */
    public void update(AllocationReceiverParam param){
        AllocationReceiver receiver = manager.findById(param.getId()).orElseThrow(() -> new PayFailureException("未找到分账接收方"));
        if (Objects.equals(receiver.getSync(),true)){
            throw new BizException("该接收方已同步到三方支付系统中,无法修改");
        }
        receiver.setSync(null);
        BeanUtil.copyProperties(param,receiver);
        manager.updateById(receiver);
    }

    /**
     * 删除分账接收方
     */
    public void remove(Long id){
        // 未同步可以删除
        AllocationReceiver receiver = manager.findById(id).orElseThrow(() -> new PayFailureException("未找到分账接收方"));
        if (Objects.equals(receiver.getSync(),true)){
            throw new BizException("该接收方已同步到三方支付系统中,无法删除");
        }
        // 判断是否绑定了分账组
        if (groupReceiverManager.isUsed(id)){
            throw new PayFailureException("该接收方已被分账组使用,无法删除");
        }
        manager.deleteById(id);
    }

    /**
     * 同步到三方支付系统中
     */
    public void registerByGateway(Long id){
        AllocationReceiver receiver = manager.findById(id).orElseThrow(() -> new PayFailureException("未找到分账接收方"));

        // 获取策略
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(receiver.getChannel());
        AbsAllocationReceiverStrategy receiverStrategy = AllocationReceiverFactory.create(channelEnum);
        // 校验
        receiverStrategy.setAllocationReceiver(receiver);
        receiverStrategy.validation();
        receiverStrategy.doBeforeHandler();
        receiverStrategy.bind();
        receiver.setSync(true);
        manager.updateById(receiver);

    }

    /**
     * 从三方支付系统中删除
     */
    public void removeByGateway(Long id){
        if (groupReceiverManager.isUsed(id)){
            throw new PayFailureException("该接收方已被使用，无法从第三方支付平台中删除");
        }

        AllocationReceiver receiver = manager.findById(id).orElseThrow(() -> new PayFailureException("未找到分账接收方"));
        // 获取策略
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(receiver.getChannel());
        AbsAllocationReceiverStrategy receiverStrategy = AllocationReceiverFactory.create(channelEnum);
        // 校验
        receiverStrategy.setAllocationReceiver(receiver);
        receiverStrategy.validation();
        receiverStrategy.doBeforeHandler();
        receiverStrategy.unbind();
        receiver.setSync(false);
        manager.updateById(receiver);
    }

}
