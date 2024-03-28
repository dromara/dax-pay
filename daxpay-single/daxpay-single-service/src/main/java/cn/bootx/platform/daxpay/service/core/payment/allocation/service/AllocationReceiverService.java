package cn.bootx.platform.daxpay.service.core.payment.allocation.service;

import cn.bootx.platform.daxpay.service.core.payment.allocation.convert.AllocationReceiverConvert;
import cn.bootx.platform.daxpay.service.core.payment.allocation.dao.AllocationReceiverManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import cn.bootx.platform.daxpay.service.param.allocation.AllocationReceiverParam;
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

    /**
     * 添加分账接收方
     */
    public void add(AllocationReceiverParam param){
        // 首先添加网关的分账接收方
        AllocationReceiver receiver = AllocationReceiverConvert.CONVERT.convert(param);
        receiver.setSync(false);
        manager.save(receiver);
    }

    /**
     * 修改
     */
    public void update(AllocationReceiverParam param){

    }

    /**
     * 删除分账接收方
     */
    public void remove(Long id){
        // 首先删除网关的分账接收方

        // 然后删除本地数据
    }

    /**
     * 同步到三方支付系统中
     */
    public void registerByGateway(Long id){

    }

    /**
     * 从三方支付系统中删除
     */
    public void removeByGateway(Long id){

    }

}
