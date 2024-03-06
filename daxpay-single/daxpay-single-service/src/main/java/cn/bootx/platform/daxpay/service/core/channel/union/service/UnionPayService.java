package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.daxpay.code.PayWayEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.UnionPayParam;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.code.AliPayWay;
import cn.bootx.platform.daxpay.service.common.context.PayLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.collection.CollUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.unionpay.UnionPayApi;
import com.ijpay.unionpay.enums.ServiceEnum;
import com.ijpay.unionpay.model.UnifiedOrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * 云闪付支付
 * @author xxm
 * @since 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayService {

    /**
     * 支付接口
     */
    public void pay(PayOrder payOrder, PayChannelParam payChannelParam, UnionPayParam unionPayParam, UnionPayConfig config){

        Integer amount = payChannelParam.getAmount();
        String totalFee = String.valueOf(amount);
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();;
        String payBody = null;
        PayWayEnum payWayEnum = PayWayEnum.findByCode(payChannelParam.getWay());

        // 二维码支付
        if (payWayEnum == PayWayEnum.QRCODE){
            payBody = this.qrCodePay(totalFee, payOrder, config);
        }

        asyncPayInfo.setPayBody(payBody);
    }

    /**
     * 扫码支付
     */
    public String qrCodePay(String totalFee, PayOrder payOrder, UnionPayConfig config){
        Map<String, String> params = UnifiedOrderModel.builder()
                .service(ServiceEnum.NATIVE.toString())
                .mch_id(config.getMachId())
                .out_trade_no(String.valueOf(payOrder.getId()))
                .body(payOrder.getTitle())
                .total_fee(totalFee)
                .time_expire(PayUtil.getUnionExpiredTime(payOrder.getExpiredTime()))
                .mch_create_ip("127.0.0.1")
                .notify_url(config.getNotifyUrl())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(config.getAppKey(), SignType.MD5);
        String xmlResult = UnionPayApi.execution(config.getServerUrl(), params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        return result.get("code_url");
    }

    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayChannelParam payChannelParam, UnionPayConfig unionPayConfig) {

        if (CollUtil.isEmpty(unionPayConfig.getPayWays())){
            throw new PayFailureException("云闪付未配置可用的支付方式");
        }
        // 发起的支付类型是否在支持的范围内
        PayWayEnum payWayEnum = Optional.ofNullable(AliPayWay.findByCode(payChannelParam.getWay()))
                .orElseThrow(() -> new PayFailureException("非法的云闪付支付类型"));
        if (!unionPayConfig.getPayWays().contains(payWayEnum.getCode())) {
            throw new PayFailureException("该云闪付支付方式不可用");
        }
    }
}
