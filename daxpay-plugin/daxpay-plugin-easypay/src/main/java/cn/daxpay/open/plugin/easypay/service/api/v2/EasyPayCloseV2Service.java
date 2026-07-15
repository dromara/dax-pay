package cn.daxpay.open.plugin.easypay.service.api.v2;

import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayCloseParam;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.param.api.v2.EasyPayCloseV2Param;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayCloseV2Result;
import cn.daxpay.open.plugin.easypay.service.api.EasyPayAssistService;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
import cn.daxpay.open.plugin.easypay.util.EasyPayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付 V2 关单
///
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayCloseV2Service {

    private final EasyPayCredentialService easyPayCredentialService;
    private final EasyPayAssistService easyPayAssistService;
    private final EasyPayOrderManager easyPayOrderManager;
    private final PayCloseService payCloseService;

    /// 关单
    public EasyPayCloseV2Result close(EasyPayCloseV2Param param) {
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        easyPayAssistService.checkSignV2(param, credential, param.getSign());
        EasyPayCloseV2Result result = new EasyPayCloseV2Result();
        if (StrUtil.isAllBlank(param.getTradeNo(), param.getOutTradeNo())) {
            result.setCode(-1).setMsg("订单号不能为空");
            return sign(result, credential);
        }
        try {
            // 优先用 out_trade_no 关单
            NormalPayCloseParam closeParam = new NormalPayCloseParam();
            if (StrUtil.isNotBlank(param.getOutTradeNo())) {
                closeParam.setBizOrderNo(param.getOutTradeNo());
            }
            // trade_no 为容器 orderNo，内核 close 的 orderNo 字段是 tradeNo —— 有 out 时优先 out
            payCloseService.close(closeParam);
            result.setCode(0).setMsg("success");
        } catch (Exception e) {
            log.error("易支付关单失败", e);
            result.setCode(-1).setMsg(e.getMessage());
        }
        result.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        return sign(result, credential);
    }

    private EasyPayCloseV2Result sign(EasyPayCloseV2Result result,
                                      cn.daxpay.open.plugin.easypay.entity.EasyPayCredential credential) {
        result.setSign(EasyPayUtil.signByRsa(result, easyPayAssistService.responsePrivateKey(credential)));
        result.setSignType("RSA");
        return result;
    }
}
