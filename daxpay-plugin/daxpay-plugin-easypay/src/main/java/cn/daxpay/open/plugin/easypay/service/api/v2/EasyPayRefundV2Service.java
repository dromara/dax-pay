package cn.daxpay.open.plugin.easypay.service.api.v2;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundService;
import cn.daxpay.open.plugin.easypay.param.api.v2.EasyPayRefundQueryV2Param;
import cn.daxpay.open.plugin.easypay.param.api.v2.EasyPayRefundV2Param;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayRefundOrderV2Result;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayRefundV2Result;
import cn.daxpay.open.plugin.easypay.service.api.EasyPayAssistService;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
import cn.daxpay.open.plugin.easypay.service.order.EasyPayRefundOrderService;
import cn.daxpay.open.plugin.easypay.util.EasyPayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/// # 易支付 V2 退款
///
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayRefundV2Service {

    private final EasyPayCredentialService easyPayCredentialService;
    private final EasyPayAssistService easyPayAssistService;
    private final RefundService payRefundService;
    private final RefundOrderManager payRefundOrderManager;
    private final EasyPayRefundOrderService easyPayRefundOrderService;

    /// 退款
    public EasyPayRefundV2Result refund(EasyPayRefundV2Param param) {
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        easyPayAssistService.checkSignV2(param, credential, param.getSign());
        EasyPayRefundV2Result result = new EasyPayRefundV2Result();
        if (StrUtil.isAllBlank(param.getTradeNo(), param.getOutTradeNo())) {
            result.setCode(-1).setMsg("订单号和商户订单号不能同时为空");
            return sign(result, credential);
        }
        try {
            RefundParam refundParam = new RefundParam();
            // 协议 out_trade_no = 商户业务单号；trade_no 可能是平台业务单号/资金号
            // 优先 bizOrderNo 解析容器，再按 tradeNo 尝试资金号/网关容器号
            refundParam.setBizOrderNo(param.getOutTradeNo());
            if (StrUtil.isNotBlank(param.getTradeNo())) {
                refundParam.setTradeNo(param.getTradeNo());
            }
            refundParam.setBizRefundNo(param.getOutRefundNo());
            refundParam.setAmount(EasyPayUtil.yuanToFen(param.getMoney()));
            refundParam.setReason("easypay refund");
            RefundOrder refundOrder = payRefundService.refund(refundParam);
            // 双写：内核退款单返回后创建/更新易支付协议退款记录
            easyPayRefundOrderService.createFromKernelRefund(refundOrder, credential);
            if (Objects.equals(refundOrder.getStatus(), RefundOrderStatusEnum.SUCCESS.getCode())) {
                result.setCode(0).setMsg("success");
            } else if (Objects.equals(refundOrder.getStatus(), RefundOrderStatusEnum.PROGRESS.getCode())) {
                result.setCode(0).setMsg("退款处理中, 需要自行稍后查询退款结果!");
            } else {
                result.setCode(-1).setMsg("退款失败");
            }
            result.setRefundNo(refundOrder.getRefundNo())
                    .setOutRefundNo(param.getOutRefundNo())
                    .setTradeNo(param.getTradeNo())
                    .setMoney(param.getMoney())
                    .setReducemoney(param.getMoney())
                    .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        } catch (Exception e) {
            log.error("易支付退款失败", e);
            result.setCode(-1).setMsg(e.getMessage())
                    .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        }
        return sign(result, credential);
    }

    /// 退款查询
    public EasyPayRefundOrderV2Result refundQuery(EasyPayRefundQueryV2Param param) {
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        easyPayAssistService.checkSignV2(param, credential, param.getSign());
        EasyPayRefundOrderV2Result result = new EasyPayRefundOrderV2Result();
        Optional<RefundOrder> opt;
        if (StrUtil.isNotBlank(param.getRefundNo())) {
            opt = payRefundOrderManager.findByRefundNo(param.getRefundNo());
        } else if (StrUtil.isNotBlank(param.getOutRefundNo())) {
            opt = payRefundOrderManager.firstOpt(q -> q
                    .eq(RefundOrder::getBizRefundNo, param.getOutRefundNo())
                    .eq(RefundOrder::getAppId, credential.getAppId()));
        } else {
            result.setCode(-1).setMsg("退款单号不能为空");
            return sign(result, credential);
        }
        if (opt.isEmpty()) {
            result.setCode(-1).setMsg("退款订单不存在");
            return sign(result, credential);
        }
        RefundOrder refund = opt.get();
        result.setCode(0)
                .setMsg("success")
                .setRefundNo(refund.getRefundNo())
                .setOutRefundNo(refund.getBizRefundNo())
                .setTradeNo(refund.getTradeNo())
                .setOutTradeNo(refund.getBizOrderNo())
                .setMoney(EasyPayUtil.fenToYuanString(refund.getAmount()))
                .setReducemoney(EasyPayUtil.fenToYuanString(refund.getAmount()))
                .setStatus(Objects.equals(refund.getStatus(), RefundOrderStatusEnum.SUCCESS.getCode()) ? 1 : 0);
        return sign(result, credential);
    }

    private EasyPayRefundV2Result sign(EasyPayRefundV2Result result,
                                       cn.daxpay.open.plugin.easypay.entity.EasyPayCredential credential) {
        result.setSign(EasyPayUtil.signByRsa(result, easyPayAssistService.responsePrivateKey(credential)));
        result.setSignType("RSA");
        return result;
    }

    private EasyPayRefundOrderV2Result sign(EasyPayRefundOrderV2Result result,
                                            cn.daxpay.open.plugin.easypay.entity.EasyPayCredential credential) {
        result.setSign(EasyPayUtil.signByRsa(result, easyPayAssistService.responsePrivateKey(credential)));
        result.setSignType("RSA");
        return result;
    }
}
