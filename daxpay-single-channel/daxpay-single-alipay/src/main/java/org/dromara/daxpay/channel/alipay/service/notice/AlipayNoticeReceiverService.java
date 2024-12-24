package org.dromara.daxpay.channel.alipay.service.notice;

import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.core.util.PayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付宝通知消息接收
 * @author xxm
 * @since 2024/5/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayNoticeReceiverService {

    private final AlipayConfigService aliPayConfigService;

    private final AlipayTransferNoticeService aliPayTransferNoticeService;

    /**
     * 通知消息处理
     */
    public String noticeReceiver(HttpServletRequest request, boolean isv) {
        Map<String, String> map = PayUtil.toMap(request);
        // 首先进行验签
        if (!aliPayConfigService.verifyNotify(map,isv)){
            log.error("支付宝消息通知验签失败");
            return "fail";
        }

        // 通过 msg_method 获取消息类型
        String msgMethod = map.get("msg_method");

        switch (msgMethod){
            // 交易分账结果通知
            case "alipay.trade.order.settle.notify"-> {
                return aliPayTransferNoticeService.transferHandle(map);
            }
            // 资金单据状态变更通知
            case "alipay.fund.trans.order.changed"->{}
            default -> log.info("消息类型:{}, 暂时无法处理", msgMethod);
        }
        return "success";
    }



}
