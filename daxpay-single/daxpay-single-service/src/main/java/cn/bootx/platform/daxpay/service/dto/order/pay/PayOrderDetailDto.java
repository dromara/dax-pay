package cn.bootx.platform.daxpay.service.dto.order.pay;

import lombok.Data;

import java.util.List;

@Data
public class PayOrderDetailDto {
    private PayOrderDto payOrder;
    private PayOrderExtraDto payOrderExtra;
    private List<PayChannelOrderDto> payChannelOrder;
}
