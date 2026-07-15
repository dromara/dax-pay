package cn.daxpay.open.plugin.easypay.service.api.v2;

import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.param.api.v2.EasyPayQueryV2Param;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayOrderV2Result;
import cn.daxpay.open.plugin.easypay.service.api.EasyPayAssistService;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
import cn.daxpay.open.plugin.easypay.util.EasyPayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/// # 易支付 V2 查单
///
@Service
@RequiredArgsConstructor
public class EasyPayQueryV2Service {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Asia/Shanghai"));

    private final EasyPayCredentialService easyPayCredentialService;
    private final EasyPayAssistService easyPayAssistService;
    private final EasyPayOrderManager easyPayOrderManager;

    /// 订单查询
    public EasyPayOrderV2Result query(EasyPayQueryV2Param param) {
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        easyPayAssistService.checkSignV2(param, credential, param.getSign());
        EasyPayOrderV2Result result = new EasyPayOrderV2Result();
        Optional<EasyPayOrder> orderOpt;
        if (StrUtil.isNotBlank(param.getTradeNo())) {
            orderOpt = easyPayOrderManager.findByTradeNo(param.getTradeNo());
        } else if (StrUtil.isNotBlank(param.getOutTradeNo())) {
            orderOpt = easyPayOrderManager.findByOutTradeNo(param.getOutTradeNo());
        } else {
            result.setCode(-1).setMsg("订单号不能为空");
            return sign(result, credential);
        }
        if (orderOpt.isEmpty()) {
            result.setCode(-1).setMsg("订单不存在");
            return sign(result, credential);
        }
        EasyPayOrder order = orderOpt.get();
        result.setCode(0)
                .setMsg("success")
                .setTradeNo(order.getTradeNo())
                .setOutTradeNo(order.getOutTradeNo())
                .setApiTradeNo(order.getApiTradeNo())
                .setType(order.getType())
                .setStatus(order.getStatus())
                .setPid(order.getPid())
                .setAddtime(order.getAddTime() == null ? null : FMT.format(order.getAddTime()))
                .setEndtime(order.getEndTime() == null ? null : FMT.format(order.getEndTime()))
                .setName(order.getName())
                .setMoney(order.getMoney() == null ? null : order.getMoney().toPlainString())
                .setParam(order.getParam())
                .setBuyer(order.getBuyer())
                .setClientip(order.getClientIp())
                .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        if (order.getRefundMoney() != null && order.getRefundMoney().compareTo(BigDecimal.ZERO) > 0) {
            result.setRefundmoney(order.getRefundMoney().toPlainString());
        }
        return sign(result, credential);
    }

    private EasyPayOrderV2Result sign(EasyPayOrderV2Result result, cn.daxpay.open.plugin.easypay.entity.EasyPayCredential credential) {
        result.setSign(EasyPayUtil.signByRsa(result, easyPayAssistService.responsePrivateKey(credential)));
        result.setSignType("RSA");
        return result;
    }
}
