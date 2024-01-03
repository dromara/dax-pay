package cn.bootx.platform.daxpay.service.common.typehandler;

import cn.bootx.platform.common.mybatisplus.handler.JacksonTypeReferenceHandler;
import cn.bootx.platform.daxpay.service.common.entity.OrderRefundableInfo;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * 支付订单可退款信息对应的MP字段处理器
 * @author xxm
 * @since 2024/1/3
 */
public class RefundableInfoTypeHandler extends JacksonTypeReferenceHandler<List<OrderRefundableInfo>> {

    /**
     * 返回要反序列化的类型对象
     */
    @Override
    public TypeReference<List<OrderRefundableInfo>> getTypeReference() {
        return new TypeReference<List<OrderRefundableInfo>>() {};
    }
}
