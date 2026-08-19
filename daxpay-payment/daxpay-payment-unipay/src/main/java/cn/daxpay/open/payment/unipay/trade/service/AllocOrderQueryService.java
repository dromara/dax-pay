package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.payment.trade.alloc.dao.AllocDetailManager;
import cn.daxpay.open.payment.trade.alloc.dao.AllocOrderManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.unipay.param.trade.alloc.AllocOrderQueryParam;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocOrderResult;
import cn.daxpay.open.payment.unipay.trade.convert.UnipayAllocOrderConvert;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 分账订单查询服务(对外)
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocOrderQueryService {

    private final AllocOrderManager allocOrderManager;
    private final AllocDetailManager allocDetailManager;

    /// 查询分账订单(含明细)
    public AllocOrderResult queryAllocOrder(AllocOrderQueryParam param) {
        // 平台分账单号和商户分账单号不能都为空
        if (StrUtil.isBlank(param.getAllocNo()) && StrUtil.isBlank(param.getBizAllocNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        AllocOrder order;
        if (StrUtil.isNotBlank(param.getAllocNo())) {
            order = allocOrderManager.findByAllocNo(param.getAllocNo())
                    .orElseThrow(() -> new DataNotExistException("pay.error.alloc.allocNotFound"));
            // 归属校验: allocNo 为全局唯一编号, 防跨商户查单
            if (!Objects.equals(order.getMchNo(), param.getMchNo())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNotBelong");
            }
        } else {
            order = allocOrderManager.findByBizAllocNo(param.getBizAllocNo(), param.getMchNo())
                    .orElseThrow(() -> new DataNotExistException("pay.error.alloc.allocNotFound"));
        }
        AllocOrderResult result = UnipayAllocOrderConvert.CONVERT.toResult(order);
        // 明细列表单独装配
        result.setDetails(UnipayAllocOrderConvert.CONVERT.toDetailResults(
                allocDetailManager.findAllByAllocNo(order.getAllocNo())));
        return result;
    }
}
