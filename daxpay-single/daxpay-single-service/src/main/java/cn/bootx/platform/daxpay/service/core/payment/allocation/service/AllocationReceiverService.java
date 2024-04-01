package cn.bootx.platform.daxpay.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.core.payment.allocation.convert.AllocationReceiverConvert;
import cn.bootx.platform.daxpay.service.core.payment.allocation.dao.AllocationGroupReceiverManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.dao.AllocationReceiverManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import cn.bootx.platform.daxpay.service.core.payment.allocation.factory.AllocationReceiverFactory;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationReceiverDto;
import cn.bootx.platform.daxpay.service.func.AbsAllocationReceiverStrategy;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationReceiverParam;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationReceiverQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        receiverStrategy.validation();

        receiver.setSync(false);
        manager.save(receiver);
    }

    /**
     * 修改信息
     */
    public void update(AllocationReceiverParam param){
        // 未同步状态可以修改


    }

    /**
     * 删除分账接收方
     */
    public void remove(Long id){

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
            throw new PayFailureException("该接收方已被使用,无法删除");
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
