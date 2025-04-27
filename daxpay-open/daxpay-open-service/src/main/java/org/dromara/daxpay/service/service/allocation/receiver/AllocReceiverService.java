package org.dromara.daxpay.service.service.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.dto.LabelValue;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.service.pay.dao.constant.ChannelConstManager;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.param.allocation.receiver.AllocReceiverQueryParam;
import org.dromara.daxpay.core.result.allocation.receiver.AllocReceiverResult;
import org.dromara.daxpay.service.convert.allocation.AllocReceiverConvert;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocGroupReceiverManager;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocReceiverManager;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import org.dromara.daxpay.service.param.allocation.receiver.AllocReceiverQuery;
import org.dromara.daxpay.service.result.allocation.receiver.AllocReceiverVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private final ChannelConstManager channelConstManager;

    /**
     * 分页
     */
    public PageResult<AllocReceiverVo> page(PageParam pageParam, AllocReceiverQuery query) {
        return MpUtil.toPageResult(allocReceiverManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public AllocReceiverVo findById(Long id) {
        return allocReceiverManager.findById(id)
                .map(AllocReceiver::toResult)
                .orElseThrow(() -> new DataNotExistException("分账接收方不存在"));
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
     * 分账方删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        // 判断是否存在
        var receiver = allocReceiverManager.findById(id).orElseThrow(() -> new DataErrorException("该接收方不存在"));
        if (groupReceiverManager.isUsed(receiver.getId())) {
            throw new OperationFailException("该接收方已被使用，无法被删除");
        }
        allocReceiverManager.deleteById(receiver.getId());
    }

    /**
     * 可分账的通道列表
     */
    public List<LabelValue> findChannels() {
        return channelConstManager.findAllByAllocatable().stream()
                .map(item->new LabelValue(item.getName(), item.getCode()))
                .toList();
    }
}
