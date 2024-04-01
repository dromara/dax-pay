package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBindModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationUnbindModel;
import com.alipay.api.domain.RoyaltyEntity;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBindResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationUnbindResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum.*;

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
     * 校验
     */
    public boolean validation(AllocationReceiver allocationReceiver){
        List<String> list = Arrays.asList(ALI_USER_ID.getCode(), ALI_OPEN_ID.getCode(), ALI_LOGIN_NAME.getCode());
        String receiverType = allocationReceiver.getReceiverType();
        return list.contains(receiverType);
    }

    /**
     * 绑定关系
     */
    @SneakyThrows
    public void bind(AllocationReceiver allocationReceiver){
        AlipayTradeRoyaltyRelationBindModel model = new AlipayTradeRoyaltyRelationBindModel();
        model.setOutRequestNo(String.valueOf(allocationReceiver.getId()));

        // 分账接收方方类型。
        RoyaltyEntity entity = new RoyaltyEntity();
        entity.setType(allocationReceiver.getReceiverType());
        entity.setAccount(allocationReceiver.getReceiverAccount());
        entity.setMemo(allocationReceiver.getRelationName());

        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationBindResponse response = AliPayApi.tradeRoyaltyRelationBind(model);

        // 如果返回成功或者已经绑定, 关系
    }

    /**
     * 解绑关系
     */
    @SneakyThrows
    public void unbind(AllocationReceiver allocationReceiver){
        AlipayTradeRoyaltyRelationUnbindModel model = new AlipayTradeRoyaltyRelationUnbindModel();
        model.setOutRequestNo(String.valueOf(allocationReceiver.getId()));

        RoyaltyEntity entity = new RoyaltyEntity();
        entity.setAccount(allocationReceiver.getReceiverAccount());
        entity.setMemo(allocationReceiver.getRelationName());

        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationUnbindResponse response =  AliPayApi.tradeRoyaltyRelationUnBind(model);
    }


}
