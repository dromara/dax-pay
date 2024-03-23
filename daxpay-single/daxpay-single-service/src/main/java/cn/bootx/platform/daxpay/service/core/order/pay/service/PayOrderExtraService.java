package cn.bootx.platform.daxpay.service.core.order.pay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2024/1/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderExtraService {
    private final PayOrderExtraManager payOrderExtraManager;

    /**
     * 查询详情
     */
    public PayOrderExtra findById(Long id){
        return payOrderExtraManager.findById(id).orElseThrow(()->new DataNotExistException("支付订单扩展信息不存在"));
    }

    /**
     * 更新, 使用单独事务
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void update(PayOrderExtra payOrderExtra){
        payOrderExtraManager.updateById(payOrderExtra);
    }

}
