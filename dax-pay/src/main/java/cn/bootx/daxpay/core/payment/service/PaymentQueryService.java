package cn.bootx.daxpay.core.payment.service;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.OrderParam;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.common.query.entity.QueryParams;
import cn.bootx.daxpay.core.payment.dao.PaymentManager;
import cn.bootx.daxpay.core.payment.entity.Payment;
import cn.bootx.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.daxpay.dto.payment.PaymentDto;
import cn.bootx.daxpay.param.payment.PaymentQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付单查询
 *
 * @author xxm
 * @date 2021/6/28
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
    public Integer findStatusByBusinessId(String businessId) {
        return paymentManager.findByBusinessId(businessId).map(Payment::getPayStatus).orElse(-1);
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
     * 根据用户id查询
     */
    public List<PaymentDto> findByUser(Long userId) {
        return paymentManager.findByUserId(userId).stream().map(Payment::toDto).collect(Collectors.toList());
    }

    /**
     * 分页
     */
    public PageResult<PaymentDto> page(PageParam pageParam, PaymentQuery param, OrderParam orderParam) {
        return MpUtil.convert2DtoPageResult(paymentManager.page(pageParam, param, orderParam));
    }

    /**
     * 超级查询
     */
    public PageResult<PaymentDto> superPage(PageParam pageParam, QueryParams queryParams) {
        return MpUtil.convert2DtoPageResult(paymentManager.superPage(pageParam, queryParams));
    }

}
