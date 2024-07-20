package cn.daxpay.multi.service.service.order.transfer;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.service.dao.order.transfer.TransferOrderManager;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.param.order.transfer.TransferOrderQuery;
import cn.daxpay.multi.service.result.order.transfer.TransferOrderResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public PageResult<TransferOrderResult> page(PageParam pageParam, TransferOrderQuery query) {
        Page<TransferOrder> page = transferOrderManager.page(pageParam, query);
        return MpUtil.toPageResult(page);
    }

    /**
     * 根据id查询
     */
    public TransferOrderResult findById(Long id) {
        return transferOrderManager.findById(id).map(TransferOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("转账订单不存在"));
    }

    /**
     * 根据转账号查询
     */
    public TransferOrderResult findByTransferNo(String transferNo){
        return transferOrderManager.findByTransferNo(transferNo).map(TransferOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("转账订单信息不存在"));

    }

    /**
     * 根据转账号查询
     */
    public TransferOrderResult findByBizTransferNo(String bizTransferNo){
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
     * 查询支付总金额
     */
    public Integer getTotalAmount(TransferOrderQuery param) {
        return transferOrderManager.getTalAmount(param);
    }
}
