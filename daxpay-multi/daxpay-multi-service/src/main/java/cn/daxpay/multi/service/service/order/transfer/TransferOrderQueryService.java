package cn.daxpay.multi.service.service.order.transfer;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.core.exception.TradeNotExistException;
import cn.daxpay.multi.core.param.trade.transfer.QueryTransferParam;
import cn.daxpay.multi.core.result.trade.transfer.TransferOrderResult;
import cn.daxpay.multi.service.convert.order.transfer.TransferOrderConvert;
import cn.daxpay.multi.service.dao.order.transfer.TransferOrderManager;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.param.order.transfer.TransferOrderQuery;
import cn.daxpay.multi.service.result.order.transfer.TransferOrderVo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * 转账订单查询服务
 * @author xxm
 * @since 2024/3/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferOrderQueryService {
    private final TransferOrderManager transferOrderManager;

    /**
     * 分页查询
     */
    public PageResult<TransferOrderVo> page(PageParam pageParam, TransferOrderQuery query) {
        Page<TransferOrder> page = transferOrderManager.page(pageParam, query);
        return MpUtil.toPageResult(page);
    }

    /**
     * 根据id查询
     */
    public TransferOrderVo findById(Long id) {
        return transferOrderManager.findById(id).map(TransferOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("转账订单不存在"));
    }

    /**
     * 根据转账号查询
     */
    public TransferOrderVo findByTransferNo(String transferNo){
        return transferOrderManager.findByTransferNo(transferNo).map(TransferOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("转账订单信息不存在"));

    }

    /**
     * 根据转账号查询
     */
    public TransferOrderVo findByBizTransferNo(String bizTransferNo){
        return transferOrderManager.findByBizTransferNo(bizTransferNo).map(TransferOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("转账订单信息不存在"));

    }

    /**
     * 根据转账号和商户转账号查询
     */
    public Optional<TransferOrder> findByBizOrTransferNo(String transferNo, String bizTransferNo) {
        if (StrUtil.isNotBlank(transferNo)){
            return transferOrderManager.findByTransferNo(transferNo);
        } else if (StrUtil.isNotBlank(bizTransferNo)){
            return transferOrderManager.findByBizTransferNo(bizTransferNo);
        } else {
            return Optional.empty();
        }
    }


    /**
     * 查询转账订单
     */
    public TransferOrderResult queryTransferOrder(QueryTransferParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getTransferNo()) && Objects.isNull(param.getBizTransferNo())){
            throw new ValidationFailedException("转账号或商户转账号不能都为空");
        }
        // 查询转账单
        TransferOrder transferOrder = this.findByBizOrTransferNo(param.getTransferNo(), param.getBizTransferNo())
                .orElseThrow(() -> new TradeNotExistException("转账订单不存在"));

        return TransferOrderConvert.CONVERT.toResult(transferOrder);
    }


    /**
     * 查询支付总金额
     */
    public BigDecimal getTotalAmount(TransferOrderQuery param) {
        return transferOrderManager.getTotalAmount(param);
    }
}
