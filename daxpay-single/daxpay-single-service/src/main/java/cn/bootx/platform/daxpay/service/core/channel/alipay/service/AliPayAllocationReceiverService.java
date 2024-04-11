package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.AliPayCode;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayResponse;
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
import java.util.Objects;

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

        RoyaltyEntity entity = new RoyaltyEntity();
        AllocationReceiverTypeEnum receiverTypeEnum = AllocationReceiverTypeEnum.findByCode(allocationReceiver.getReceiverType());
        entity.setType(receiverTypeEnum.getOutCode());
        entity.setAccount(allocationReceiver.getReceiverAccount());
        entity.setName(allocationReceiver.getReceiverName());
        entity.setMemo(allocationReceiver.getRelationName());

        // 不报错视为同步成功
        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationBindResponse response = AliPayApi.tradeRoyaltyRelationBind(model);
        this.verifyErrorMsg(response);
    }

    /**
     * 解绑关系
     */
    @SneakyThrows
    public void unbind(AllocationReceiver allocationReceiver){
        AlipayTradeRoyaltyRelationUnbindModel model = new AlipayTradeRoyaltyRelationUnbindModel();
        model.setOutRequestNo(String.valueOf(allocationReceiver.getId()));

        RoyaltyEntity entity = new RoyaltyEntity();
        AllocationReceiverTypeEnum receiverTypeEnum = findByCode(allocationReceiver.getReceiverType());
        entity.setType(receiverTypeEnum.getOutCode());
        entity.setAccount(allocationReceiver.getReceiverAccount());

        model.setReceiverList(Collections.singletonList(entity));
        AlipayTradeRoyaltyRelationUnbindResponse response =  AliPayApi.tradeRoyaltyRelationUnBind(model);
        System.out.println(response);
        // 如果出现分账方不存在也视为成功
        if (Objects.equals(response.getSubCode(), AliPayCode.USER_NOT_EXIST)) {
            return;
        }
        this.verifyErrorMsg(response);
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(AlipayResponse alipayResponse) {
        if (!Objects.equals(alipayResponse.getCode(), AliPayCode.SUCCESS)) {
            String errorMsg = alipayResponse.getSubMsg();
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = alipayResponse.getMsg();
            }
            log.error("分账接收方处理失败 {}", errorMsg);
            throw new PayFailureException(errorMsg);
        }
    }
}
