package org.dromara.daxpay.payment.miniapp.service;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.payment.miniapp.param.order.MiniPayOrderQuery;
import org.dromara.daxpay.payment.miniapp.param.order.MiniRefundOrderQuery;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.pay.result.order.pay.PayOrderVo;
import org.dromara.daxpay.payment.pay.result.order.refund.RefundOrderVo;
import org.dromara.daxpay.payment.pay.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.payment.pay.service.order.refund.RefundOrderQueryService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 小程序订单查询服务
 * @author xxm
 * @since 2025/4/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MiniMchOrderQueryService {
    private final PayOrderQueryService payOrderQueryService;
    private final PayOrderManager payOrderManager;
    private final RefundOrderQueryService refundOrderQueryService;
    private final RefundOrderManager refundOrderManager;

    /**
     * 支付订单列表
     */
    public PageResult<PayOrderVo> pageByPay(MiniPayOrderQuery query){
        var queryChainWrapper = payOrderManager.lambdaQuery()
                .eq(StrUtil.isNotBlank(query.getAppId()), PayOrder::getAppId, query.getAppId())
                .in(CollUtil.isNotEmpty(query.getPayStatus()), PayOrder::getStatus, query.getPayStatus())
                .in(CollUtil.isNotEmpty(query.getChannel()), PayOrder::getChannel, query.getChannel());
        if (ObjectUtil.isAllNotEmpty(query.getStartTime(), query.getEndTime())){
            var beginOfDay = LocalDateTimeUtil.beginOfDay(query.getStartTime());
            var endOfDay = LocalDateTimeUtil.endOfDay(query.getEndTime());
            queryChainWrapper.between(PayOrder::getPayTime, beginOfDay, endOfDay);
        }
        // 订单号
        if (StrUtil.isNotBlank(query.getOrderNo())){
            queryChainWrapper.like(PayOrder::getOrderNo, query.getOrderNo())
                    .or().like(PayOrder::getBizOrderNo, query.getOrderNo())
                    .or().like(PayOrder::getOutOrderNo, query.getOrderNo());
        }

        Page<PayOrder> page = queryChainWrapper.page(MpUtil.getMpPage(query));
        return MpUtil.toPageResult(page);
    }

    /**
     * 支付订单详情
     */
    public PayOrderVo findPayOrderById(Long id){
        return payOrderQueryService.findById(id)
                .map(PayOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
    }

    /**
     * 根据订单编号(业务/通道/平台)查询支付订单信息
     */
    public PayOrderVo findPayOrderByNo(String orderNo, String appId) {
        return payOrderQueryService.findAnyOrderNo(orderNo,appId)
                .map(PayOrder::toResult)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));
    }

    /**
     * 退款订单列表
     */
    public PageResult<RefundOrderVo> pageByRefund(MiniRefundOrderQuery query){
        var queryChainWrapper = refundOrderManager.lambdaQuery()
                .eq(StrUtil.isNotBlank(query.getAppId()), RefundOrder::getAppId, query.getAppId())
                .in(CollUtil.isNotEmpty(query.getRefundStatus()), RefundOrder::getStatus, query.getRefundStatus())
                .in(CollUtil.isNotEmpty(query.getChannel()), RefundOrder::getChannel, query.getChannel());
        if (ObjectUtil.isAllNotEmpty(query.getStartTime(), query.getEndTime())){
            var beginOfDay = LocalDateTimeUtil.beginOfDay(query.getStartTime());
            var endOfDay = LocalDateTimeUtil.endOfDay(query.getEndTime());
            queryChainWrapper.between(RefundOrder::getFinishTime, beginOfDay, endOfDay);
        }
        // 退款号
        if (StrUtil.isNotBlank(query.getRefundNo())){
            queryChainWrapper.like(RefundOrder::getRefundNo, query.getRefundNo())
                    .or().like(RefundOrder::getOutRefundNo, query.getRefundNo())
                    .or().like(RefundOrder::getBizRefundNo, query.getRefundNo());
        }
        Page<RefundOrder> page =queryChainWrapper.page(MpUtil.getMpPage(query));
        return MpUtil.toPageResult(page);
    }

    /**
     * 退款订单详情
     */
    public RefundOrderVo findRefundOrderById(Long id){
        return refundOrderQueryService.findById(id);
    }

}
