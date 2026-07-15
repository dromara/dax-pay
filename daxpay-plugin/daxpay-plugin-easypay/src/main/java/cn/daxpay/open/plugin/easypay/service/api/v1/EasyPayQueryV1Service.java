package cn.daxpay.open.plugin.easypay.service.api.v1;

import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.param.api.v1.EasyPayQueryV1Param;
import cn.daxpay.open.plugin.easypay.result.api.v1.EasyPayOrderV1Result;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/// # 易支付 V1 查单（api.php?act=order）
///
@Service
@RequiredArgsConstructor
public class EasyPayQueryV1Service {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("Asia/Shanghai"));

    private final EasyPayCredentialService easyPayCredentialService;
    private final EasyPayOrderManager easyPayOrderManager;

    public EasyPayOrderV1Result query(EasyPayQueryV1Param param) {
        EasyPayOrderV1Result result = new EasyPayOrderV1Result();
        if (!Objects.equals(param.getAct(), "order")) {
            result.setCode(-1).setMsg("不支持的 act");
            return result;
        }
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        if (!credential.getEnableV1()) {
            throw new ValidationFailedException("error.plugin.easypay.v1Disabled");
        }
        // V1 弱鉴权：key = md5Key
        if (!Objects.equals(param.getKey(), credential.getMd5Key())) {
            throw new ValidationFailedException("error.plugin.easypay.signInvalid");
        }
        Optional<EasyPayOrder> orderOpt;
        if (StrUtil.isNotBlank(param.getTradeNo())) {
            orderOpt = easyPayOrderManager.findByTradeNo(param.getTradeNo());
        } else if (StrUtil.isNotBlank(param.getOutTradeNo())) {
            orderOpt = easyPayOrderManager.findByOutTradeNo(param.getOutTradeNo());
        } else {
            result.setCode(-1).setMsg("订单号不能为空");
            return result;
        }
        if (orderOpt.isEmpty()) {
            result.setCode(-1).setMsg("订单不存在");
            return result;
        }
        EasyPayOrder order = orderOpt.get();
        result.setCode(1)
                .setMsg("success")
                .setTradeNo(order.getTradeNo())
                .setOutTradeNo(order.getOutTradeNo())
                .setType(order.getType())
                .setStatus(order.getStatus())
                .setPid(order.getPid())
                .setAddtime(order.getAddTime() == null ? null : FMT.format(order.getAddTime()))
                .setEndtime(order.getEndTime() == null ? null : FMT.format(order.getEndTime()))
                .setName(order.getName())
                .setMoney(order.getMoney() == null ? null : order.getMoney().toPlainString())
                .setParam(order.getParam())
                .setBuyer(order.getBuyer());
        return result;
    }
}
