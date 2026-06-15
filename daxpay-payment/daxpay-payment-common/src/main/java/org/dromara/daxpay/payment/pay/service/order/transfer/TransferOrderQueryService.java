package org.dromara.daxpay.payment.pay.service.order.transfer;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;

import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.payment.pay.exception.TradeNotExistException;
import org.dromara.daxpay.payment.unipay.param.trade.transfer.QueryTransferParam;
import org.dromara.daxpay.payment.unipay.result.trade.transfer.TransferOrderResult;
import org.dromara.daxpay.payment.pay.convert.order.transfer.TransferOrderConvert;
import org.dromara.daxpay.payment.pay.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.pay.param.order.transfer.TransferOrderQuery;
import org.dromara.daxpay.payment.pay.result.order.transfer.TransferOrderVo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/// # 转账订单查询服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferOrderQueryService {
    private final TransferOrderManager transferOrderManager;

    /// 分页查询
    public PageResult<TransferOrderVo> page(PageParam pageParam, TransferOrderQuery query) {
        Page<TransferOrder> page = transferOrderManager.page(pageParam, query);
        return MpUtil.toPageResult(page);
    }

    /// 根据id查询
    public TransferOrderVo findById(Long id) {
        return transferOrderManager.findById(id).map(TransferOrder::toResult)
                // 订单: 转账订单不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.transferOrderNotExist"));
    }

    /// 根据转账号查询
    public TransferOrderVo findByTransferNo(String transferNo){
        return transferOrderManager.findByTransferNo(transferNo).map(TransferOrder::toResult)
                // 订单: 转账订单信息不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.order.transferOrderInfoNotExist"));

    }

    /// 根据转账号和商户转账号查询
    public Optional<TransferOrder> findByBizOrTransferNo(String transferNo, String bizTransferNo, String appId) {
        if (StrUtil.isNotBlank(transferNo)){
            return transferOrderManager.findByTransferNo(transferNo);
        } else if (StrUtil.isNotBlank(bizTransferNo)){
            return transferOrderManager.findByBizTransferNo(bizTransferNo,appId);
        } else {
            return Optional.empty();
        }
    }

    /// 查询转账订单
    public TransferOrderResult queryTransferOrder(QueryTransferParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getTransferNo()) && Objects.isNull(param.getBizTransferNo())){
            // 转账号或商户转账号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        // 查询转账单
        TransferOrder transferOrder = this.findByBizOrTransferNo(param.getTransferNo(), param.getBizTransferNo(), param.getAppId())
                // 订单: 转账订单不存在
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.transferOrderNotExist"));

        return TransferOrderConvert.CONVERT.toResult(transferOrder);
    }

    /// 查询支付总金额
    public BigDecimal getTotalAmount(TransferOrderQuery param) {
        return transferOrderManager.getTotalAmount(param);
    }

    
    

}
