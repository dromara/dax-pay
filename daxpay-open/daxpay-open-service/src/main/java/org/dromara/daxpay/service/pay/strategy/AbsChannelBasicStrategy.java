package org.dromara.daxpay.service.pay.strategy;

import cn.bootx.platform.core.rest.dto.KeyValue;

import java.util.List;

/**
 * 通道基础数据获取策略
 * @author xxm
 * @since 2025/6/4
 */
public abstract class AbsChannelBasicStrategy implements PaymentStrategy{

    /**
     * 获取通道的支付列表
     */
    public abstract List<KeyValue> payMethodList();

}
