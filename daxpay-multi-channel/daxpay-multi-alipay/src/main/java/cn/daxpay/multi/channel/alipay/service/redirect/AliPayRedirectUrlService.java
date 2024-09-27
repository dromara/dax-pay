package cn.daxpay.multi.channel.alipay.service.redirect;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.daxpay.multi.channel.alipay.code.AliPayCode;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.dao.order.pay.PayOrderManager;
import cn.daxpay.multi.service.entity.config.PlatformConfig;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.service.config.PlatformConfigService;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 同步通知回调处理
 * @author xxm
 * @since 2024/9/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayRedirectUrlService {
    private final AliPayConfigService aliPayConfigService;
    private final PayOrderManager payOrderManager;
    private final PlatformConfigService platformConfigService;

    /**
     * 回调地址处理
     */
    public String redirect(HttpServletRequest request){
        Map<String, String> map = PayUtil.toMap(request);
        // 首先进行验签
        if (!aliPayConfigService.verifyNotify(map)){
            log.error("支付宝同步通知消息验签失败");
            throw new ValidationFailedException("支付宝同步通知消息验签失败");
        }
        // 获取订单号
        String outTradeNo = map.get(AliPayCode.ResponseParams.OUT_TRADE_NO);
        PayOrder order = payOrderManager.findByOrderNo(outTradeNo)
                .orElseThrow(() -> new DataNotExistException("订单不存在"));
        // 订单配置后跳转到指定的地址, 不存在跳转到预设的地址
        if (StrUtil.isNotBlank(order.getReturnUrl())){
            return StrUtil.format("{}?biz_trade_no={}&trade_no={}", order.getReturnUrl(),order.getBizOrderNo(),order.getOrderNo() );
        } else {
            PlatformConfig platformConfig = platformConfigService.getConfig();
            if (platformConfig.isMobileEmbedded()){
                // 嵌入式
                String serverUrl = platformConfig.getGatewayMobileUrl();
                return StrUtil.format("{}/h5/alipay/pay/finish", serverUrl);

            } else {
                // 独立部署
                String serverUrl = platformConfig.getGatewayMobileUrl();
                return StrUtil.format("{}/alipay/pay/finish", serverUrl);

            }
        }
    }
}
