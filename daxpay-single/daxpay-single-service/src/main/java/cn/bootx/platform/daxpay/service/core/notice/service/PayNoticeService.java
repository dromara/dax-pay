package cn.bootx.platform.daxpay.service.core.notice.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.code.PaySignTypeEnum;
import cn.bootx.platform.daxpay.service.common.context.ApiInfoLocal;
import cn.bootx.platform.daxpay.service.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.notice.result.PayChannelResult;
import cn.bootx.platform.daxpay.service.core.notice.result.PayNoticeResult;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.util.PaySignUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付相关消息通知
 * @author xxm
 * @since 2024/1/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayNoticeService {
    private final PayOrderQueryService payOrderQueryService;
    private final PayOrderExtraManager payOrderExtraManager;
    private final PayChannelOrderManager payChannelOrderManager;

    /**
     * 发送支付完成回调通知
     */
    public void sendPayCallbackNotice(Long paymentId){
        try {
            // 获取通知地址和内容相关的信息
            ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();
            PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
            // 首先判断接口是开启了通知回调功能
            if (apiInfo.isNotice()){
                PayOrder payOrder = payOrderQueryService.findById(paymentId).orElseThrow(DataNotExistException::new);
                // 判断是否是同步支付, 并且配置不进行消息通知
                if (!payOrder.isAsyncPay() && apiInfo.isOnlyAsyncNotice()){
                    return;
                }
                // 判断客户端发送的请求是否需要发送回调通知
                PayOrderExtra payOrderExtra = payOrderExtraManager.findById(paymentId).orElseThrow(DataNotExistException::new);
                if (payOrderExtra.isNotNotify()){
                    return;
                }
                List<PayChannelResult> orderChannels = payChannelOrderManager.findAllByPaymentId(paymentId).stream()
                        .map(o->new PayChannelResult().setChannel(o.getChannel()).setWay(o.getPayWay()).setAmount(o.getAmount()))
                        .collect(Collectors.toList());
                // 组装内容
                PayNoticeResult payNoticeResult = new PayNoticeResult()
                        .setPaymentId(payOrder.getId())
                        .setAsyncPay(payOrder.isAsyncPay())
                        .setBusinessNo(payOrder.getBusinessNo())
                        .setAmount(payOrder.getAmount())
                        .setPayTime(payOrder.getPayTime())
                        .setCreateTime(payOrder.getCreateTime())
                        .setStatus(payOrder.getStatus())
                        .setAttach(payOrderExtra.getAttach())
                        .setPayChannels(orderChannels);
                // 是否需要签名
                if (apiInfo.isNoticeSign()){
                    // 签名
                    if (Objects.equals(platform.getSignType(), PaySignTypeEnum.MD5.getCode())){
                        payNoticeResult.setSign(PaySignUtil.md5Sign(payNoticeResult,platform.getSignSecret()));
                    } else {
                        payNoticeResult.setSign(PaySignUtil.hmacSha256Sign(payNoticeResult,platform.getSignSecret()));
                    }
                }
                // 地址
                String notifyUrl = payOrderExtra.getNotifyUrl();
                if (StrUtil.isBlank(notifyUrl)){
                    throw new DataNotExistException("通知地址为空");
                }
                // 发送请求数据
                HttpResponse execute = HttpUtil.createPost(notifyUrl)
                        .body(JSONUtil.toJsonStr(payNoticeResult), ContentType.JSON.getValue())
                        .timeout(10000)
                        .execute();
                log.info(execute.body());
            }

        } catch (Exception e) {
            log.error("发送失败",e);
            // 记录错误信息, 同时配置下次通知的时间

        }

    }
}
