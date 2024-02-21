package cn.bootx.platform.daxpay.service.core.order.refund.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.param.pay.QueryRefundParam;
import cn.bootx.platform.daxpay.result.order.RefundChannelOrderResult;
import cn.bootx.platform.daxpay.result.order.RefundOrderResult;
import cn.bootx.platform.daxpay.service.core.order.refund.convert.RefundOrderConvert;
import cn.bootx.platform.daxpay.service.core.order.refund.convert.RefundOrderChannelConvert;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundOrderDto;
import cn.bootx.platform.daxpay.service.dto.order.refund.RefundChannelOrderDto;
import cn.bootx.platform.daxpay.service.param.order.RefundOrderQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 退款
 *
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderQueryService {

    private final RefundOrderManager refundOrderManager;
    private final RefundChannelOrderManager refundOrderChannelManager;

    /**
     * 分页查询
     */
    public PageResult<RefundOrderDto> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> page = refundOrderManager.page(pageParam, query);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public RefundOrderDto findById(Long id) {
        return refundOrderManager.findById(id).map(RefundOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("退款订单不存在"));
    }

    /**
     * 通道退款订单列表查询
     */
    public List<RefundChannelOrderDto> listByChannel(Long refundId){
        List<RefundChannelOrder> refundOrderChannels = refundOrderChannelManager.findAllByRefundId(refundId);
        return refundOrderChannels.stream()
                .map(RefundOrderChannelConvert.CONVERT::convert)
                .collect(Collectors.toList());
    }

    /**
     * 查询通道退款订单详情
     */
    public RefundChannelOrderDto findChannelById(Long id) {
        return refundOrderChannelManager.findById(id)
                .map(RefundChannelOrder::toDto)
                .orElseThrow(() -> new DataNotExistException("通道退款订单不存在"));
    }

    /**
     * 查询退款订单
     */
    public RefundOrderResult queryRefundOrder(QueryRefundParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getRefundNo()) && Objects.isNull(param.getRefundId())){
            throw new ValidationFailedException("退款号或退款ID不能都为空");
        }

        // 查询退款单
        RefundOrder refundOrder = null;
        if (Objects.nonNull(param.getRefundId())){
            refundOrder = refundOrderManager.findById(param.getRefundId())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        if (Objects.isNull(refundOrder)){
            refundOrder = refundOrderManager.findByRefundNo(param.getRefundNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        // 查询退款明细
        List<RefundChannelOrder> refundOrderChannels = refundOrderChannelManager.findAllByRefundId(refundOrder.getId());
        List<RefundChannelOrderResult> channels = refundOrderChannels.stream()
                .map(RefundOrderChannelConvert.CONVERT::convertResult)
                .collect(Collectors.toList());

        RefundOrderResult refundOrderResult = RefundOrderConvert.CONVERT.convertResult(refundOrder);
        refundOrderResult.setRefundId(refundOrder.getId());
        refundOrderResult.setChannels(channels);
        return refundOrderResult;
    }
}
