package cn.bootx.platform.daxpay.core.payment.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.entity.QueryOrder;
import cn.bootx.platform.common.query.entity.QueryParams;
import cn.bootx.platform.daxpay.core.payment.dao.PaymentManager;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.platform.daxpay.dto.payment.PaymentDto;
import cn.bootx.platform.daxpay.param.payment.PaymentQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cn.bootx.platform.daxpay.code.pay.PayStatusCode.TRADE_UNKNOWN;

/**
 * 支付单查询
 *
 * @author xxm
 * @since 2021/6/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentQueryService {

    private final PaymentManager paymentManager;

    /**
     * 根据支付Id查询支付单
     */
    public PaymentDto findById(Long id) {
        return paymentManager.findById(id).map(Payment::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据业务ID获取支付状态
     */
    public String findStatusByBusinessId(String businessId) {
        return paymentManager.findByBusinessId(businessId).map(Payment::getPayStatus).orElse(TRADE_UNKNOWN);
    }

    /**
     * 根据businessId获取订单支付方式
     */
    public List<PayChannelInfo> findPayTypeInfoByBusinessId(String businessId) {
        return paymentManager.findByBusinessId(businessId).map(Payment::getPayChannelInfo).orElse(new ArrayList<>(1));
    }

    /**
     * 根据id获取订单支付方式
     */
    public List<PayChannelInfo> findPayTypeInfoById(Long id) {
        return paymentManager.findById(id).map(Payment::getPayChannelInfo).orElse(new ArrayList<>(1));
    }

    /**
     * 分页
     */
    public PageResult<PaymentDto> page(PageParam pageParam, PaymentQuery param, QueryOrder queryOrder) {
        return MpUtil.convert2DtoPageResult(paymentManager.page(pageParam, param, queryOrder));
    }

    /**
     * 超级查询
     */
    public PageResult<PaymentDto> superPage(PageParam pageParam, QueryParams queryParams) {
        return MpUtil.convert2DtoPageResult(paymentManager.superPage(pageParam, queryParams));
    }

}
