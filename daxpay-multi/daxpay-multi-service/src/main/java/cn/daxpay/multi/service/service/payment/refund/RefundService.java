package cn.daxpay.multi.service.service.payment.refund;

import cn.daxpay.multi.service.dao.order.refund.RefundOrderManager;
import cn.daxpay.multi.service.service.order.pay.PayOrderQueryService;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付退款服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {


    private final RefundOrderManager refundOrderManager;

    private final PayOrderQueryService payOrderQueryService;


    private final LockTemplate lockTemplate;



}
