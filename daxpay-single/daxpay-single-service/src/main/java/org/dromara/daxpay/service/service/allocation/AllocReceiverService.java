package org.dromara.daxpay.service.service.allocation;

import cn.bootx.platform.baseapi.service.dict.DictionaryItemService;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.exception.OperationProcessingException;
import org.dromara.daxpay.core.param.allocation.receiver.AllocReceiverAddParam;
import org.dromara.daxpay.core.param.allocation.receiver.AllocReceiverQueryParam;
import org.dromara.daxpay.core.param.allocation.receiver.AllocReceiverRemoveParam;
import org.dromara.daxpay.core.result.allocation.receiver.AllocReceiverResult;
import org.dromara.daxpay.service.bo.allocation.AllocReceiverResultBo;
import org.dromara.daxpay.service.convert.allocation.AllocReceiverConvert;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocGroupReceiverManager;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocReceiverManager;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.dromara.daxpay.service.param.allocation.receiver.AllocReceiverQuery;
import org.dromara.daxpay.service.strategy.AbsAllocReceiverStrategy;
import org.dromara.daxpay.service.strategy.PaymentStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 分账接收方服务类
 * @author xxm
 * @since 2024/3/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocReceiverService {

    private final AllocGroupReceiverManager groupReceiverManager;

    private final AllocReceiverManager allocReceiverManager;

    private final LockTemplate lockTemplate;
    private final DictionaryItemService dictionaryItemService;

    /**
     * 分页
     */
    public PageResult<AllocReceiverResultBo> page(PageParam pageParam, AllocReceiverQuery query) {
        return MpUtil.toPageResult(allocReceiverManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public AllocReceiverResultBo findById(Long id) {
        return allocReceiverManager.findById(id)
                .map(AllocReceiver::toResult)
                .orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
    }

    /**
     * 编码是否存在
     */
    public boolean existsByReceiverNo(String receiverNo, String appId) {
        return allocReceiverManager.existedByReceiverNo(receiverNo, appId);
    }


    /**
     * 分账接收方列表
     */
    public AllocReceiverResult list(AllocReceiverQueryParam param){
        List<AllocReceiver> allocReceivers = allocReceiverManager.findAllByChannel(param.getChannel(), param.getAppId());
        List<AllocReceiverResult.Receiver> list = AllocReceiverConvert.CONVERT.toList(allocReceivers);
        return new AllocReceiverResult().setReceivers(list);
    }


    /**
     * 添加分账接收方并同步到三方支付系统中
     */
    @Transactional(rollbackFor = Exception.class)
    public void addAndSync(AllocReceiverAddParam param) {
        // 判断是否已经添加
        LockInfo lock = lockTemplate.lock("payment:receiver:" + param.getReceiverNo(), 10000, 200);
        if (Objects.isNull(lock)) {
            throw new OperationProcessingException("分账方处理中，请勿重复操作");
        }
        try {
            Optional<AllocReceiver> receiverOptional = allocReceiverManager.findByReceiverNo(param.getReceiverNo(),param.getAppId());
            if (receiverOptional.isPresent()) {
                throw new OperationFailException("该接收方已存在");
            }
            AllocReceiver receiver = AllocReceiverConvert.CONVERT.convert(param);
            // 预先写入id, 部分通道需要使用外部请求号, 用id进行关联
            receiver.setId(IdUtil.getSnowflakeNextId());
            // 获取策略
            AbsAllocReceiverStrategy receiverStrategy = PaymentStrategyFactory.create(param.getChannel(), AbsAllocReceiverStrategy.class);
            // 校验
            receiverStrategy.setAllocReceiver(receiver);
            if (!receiverStrategy.validation()) {
                throw new ValidationFailedException("接收方信息校验失败");
            }
            // 先添加到三方支付系统中, 然后保存到本地
            receiverStrategy.doBeforeHandler();
            receiverStrategy.bind();
            allocReceiverManager.save(receiver);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 分账方删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeAndSync(AllocReceiverRemoveParam param) {
        // 判断是否存在
        AllocReceiver receiver = allocReceiverManager.findByReceiverNo(param.getReceiverNo(), param.getAppId())
                .orElseThrow(() -> new DataErrorException("该接收方不存在"));
        if (groupReceiverManager.isUsed(receiver.getId())) {
            throw new OperationFailException("该接收方已被使用，无法被删除");
        }
        // 获取策略
        var receiverStrategy = PaymentStrategyFactory.create(receiver.getChannel(), AbsAllocReceiverStrategy.class);
        LockInfo lock = lockTemplate.lock("payment:receiver:" + param.getReceiverNo(), 10000, 200);
        if (Objects.isNull(lock)) {
            throw new OperationProcessingException("分账方处理中，请勿重复操作");
        }
        try {
            receiverStrategy.setAllocReceiver(receiver);
            // 校验
            receiverStrategy.validation();
            // 预处理
            receiverStrategy.doBeforeHandler();
            // 取消绑定
            receiverStrategy.unbind();
            allocReceiverManager.deleteById(receiver.getId());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 可分账的通道列表
     */
    public List<LabelValue> findChannels() {
        // 先查询策略, 然后查询通道并进行过滤
        List<String> channelCodes = SpringUtil.getBeansOfType(AbsAllocReceiverStrategy.class)
                .values()
                .stream()
                .map(PaymentStrategy::getChannel)
                .toList();
        return dictionaryItemService.findEnableByDictCode("channel").stream()
                .filter(item -> channelCodes.contains(item.getCode()))
                .map(item -> new LabelValue(item.getName(), item.getCode()))
                .toList();
    }

    /**
     * 根据通道获取分账接收方类型
     */
    public List<LabelValue> findReceiverTypeByChannel(String channel) {
        var receiverStrategy = PaymentStrategyFactory.create(channel, AbsAllocReceiverStrategy.class);
        return receiverStrategy.getSupportReceiverTypes().stream()
                .map(o-> new LabelValue(o.getName(), o.getCode()))
                .toList();
    }
}
