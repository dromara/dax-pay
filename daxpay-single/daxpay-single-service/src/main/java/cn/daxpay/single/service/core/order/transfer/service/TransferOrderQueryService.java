package cn.daxpay.single.service.core.order.transfer.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.core.exception.ParamValidationFailException;
import cn.daxpay.single.core.exception.PayFailureException;
import cn.daxpay.single.core.exception.TradeNotExistException;
import cn.daxpay.single.core.param.payment.transfer.QueryTransferParam;
import cn.daxpay.single.core.result.order.TransferOrderResult;
import cn.daxpay.single.service.core.order.transfer.convert.TransferOrderConvert;
import cn.daxpay.single.service.core.order.transfer.dao.TransferOrderManager;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.dto.order.transfer.TransferOrderDto;
import cn.daxpay.single.service.param.order.TransferOrderQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public PageResult<TransferOrderDto> page(PageParam pageParam, TransferOrderQuery query) {
        Page<TransferOrder> page = transferOrderManager.page(pageParam, query);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public TransferOrderDto findById(Long id) {
        return transferOrderManager.findById(id).map(TransferOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("转账订单不存在"));
    }

    /**
     * 根据转账号查询
     */
    public TransferOrderDto findByTransferNo(String transferNo){
        return transferOrderManager.findByTransferNo(transferNo).map(TransferOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("转账订单扩展信息不存在"));

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
            throw new ParamValidationFailException("转账号或商户转账号不能都为空");
        }
        // 查询转账单
        TransferOrder transferOrder = this.findByBizOrTransferNo(param.getTransferNo(), param.getBizTransferNo())
                .orElseThrow(() -> new TradeNotExistException("转账订单不存在"));

        return TransferOrderConvert.CONVERT.convertResult(transferOrder);
    }

    /**
     * 查询支付总金额
     */
    public Integer getTotalAmount(TransferOrderQuery param) {
        return transferOrderManager.getTalAmount(param);
    }
}
