package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.unionpay.enums.ServiceEnum;
import com.ijpay.unionpay.model.CloseOrderModel;
import com.ijpay.wxpay.WxPayApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static cn.bootx.platform.daxpay.service.code.UnionPayCode.*;

/**
 * 云闪付支付关闭
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayCloseService {

    /**
     * 关闭订单
     */
    public void close(PayOrder payOrder, UnionPayConfig unionPayConfig) {
        Map<String, String> params = CloseOrderModel.builder()
                .service(ServiceEnum.CLOSE.toString())
                .mch_id(unionPayConfig.getMachId())
                .out_trade_no(String.valueOf(payOrder.getId()))
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(unionPayConfig.getAppKey(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.closeOrder(params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(Map<String, String> result) {
        String status = result.get(UnionPayCode.STATUS);
        String returnCode = result.get(UnionPayCode.RESULT_CODE);

        // 判断查询是否成功
        if (!(Objects.equals(SUCCESS, status) && Objects.equals(SUCCESS, returnCode))){
            String errorMsg = result.get(ERR_MSG);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(MESSAGE);
            }
            log.error("订单关闭失败 {}", errorMsg);
            throw new PayFailureException(errorMsg);
        }
    }
}
