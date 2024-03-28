package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBindModel;
import com.alipay.api.domain.RoyaltyEntity;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBindResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 支付宝分账
 * @author xxm
 * @since 2024/3/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayAllocationReceiverService {

    /**
     * 注册
     */
    @SneakyThrows
    public void bind(AllocationReceiver allocationReceiver){
        AlipayTradeRoyaltyRelationBindModel model = new AlipayTradeRoyaltyRelationBindModel();
        model.setOutRequestNo(String.valueOf(allocationReceiver.getId()));

        RoyaltyEntity entity = new RoyaltyEntity();
        entity.setAccount(allocationReceiver.getReceiverAccount());
        entity.setMemo(allocationReceiver.getRelationName());

        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationBindResponse response = AliPayApi.tradeRoyaltyRelationBind(model);
    }
}
