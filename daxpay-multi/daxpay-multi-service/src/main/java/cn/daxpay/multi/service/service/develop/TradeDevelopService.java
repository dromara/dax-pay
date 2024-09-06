package cn.daxpay.multi.service.service.develop;

import cn.daxpay.multi.core.result.trade.pay.PayResult;
import cn.daxpay.multi.core.result.trade.refund.RefundResult;
import cn.daxpay.multi.core.result.trade.transfer.TransferResult;
import cn.daxpay.multi.service.service.trade.pay.PayService;
import cn.daxpay.multi.service.service.trade.refund.RefundService;
import cn.daxpay.multi.service.service.trade.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 交易开发调试服务商
 * @author xxm
 * @since 2024/9/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeDevelopService {
    private final PayService payService;
    private final RefundService refundService;
    private final TransferService transferService;

    /**
     * 支付交易
     */
    public PayResult pay(){

        return null;
    }

    /**
     * 退款交易
     */
    public RefundResult refund(){

        return null;
    }

    /**
     * 转账交易
     */
    public TransferResult transfer(){
        return null;
    }

}
