package cn.bootx.platform.daxpay.service.core.channel.voucher.service;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.service.core.channel.voucher.dao.VoucherManager;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 储值卡支付
 *
 * @author xxm
 * @since 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherPayService {

    private final VoucherManager voucherManager;

    private final VoucherQueryService voucherQueryService;


    /**
     * 支付
     */
    @Transactional(rollbackFor = Exception.class)
    public void pay(Integer amount, Voucher voucher) {
        // 记录当前卡扣了多少钱 储值卡余额-待支付额
        voucher.setBalance(voucher.getBalance()-amount);
        voucherManager.updateById(voucher);
    }


    /**
     * 关闭支付,解除冻结的额度, 只有在异步支付中才会发生关闭操作
     */
    public void close(PayChannelOrder channelOrder) {
        // 获取使用的储值卡参数
        String channelExtra = channelOrder.getChannelExtra();
        VoucherPayParam voucherPayParam = JSONUtil.toBean(channelExtra, VoucherPayParam.class);
        Voucher voucher = voucherManager.findByCardNo(voucherPayParam.getCardNo())
                .orElseThrow(() -> new PayFailureException("储值卡不存在"));
        voucher.setBalance(voucher.getBalance()+channelOrder.getAmount());
        voucherManager.updateById(voucher);
    }

    /**
     * 退款
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund(int amount, Voucher voucher) {
        voucher.setBalance(voucher.getBalance()+amount);
        voucherManager.updateById(voucher);
    }
}
