package cn.bootx.platform.daxpay.core.channel.voucher.service;

import cn.bootx.platform.daxpay.code.VoucherCode;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherLogManager;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherManager;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherPaymentManager;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherLog;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherPayOrder;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherRecord;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
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

    private final VoucherPaymentManager voucherPaymentManager;

    private final VoucherLogManager voucherLogManager;

    private final VoucherQueryService voucherQueryService;

    /**
     * 获取并检查储值卡
     */
    public Voucher getAndCheckVoucher(PayWayParam payWayParam) {
        VoucherPayParam voucherPayParam;
        try {
            // 储值卡参数验证
            String extraParamsJson = payWayParam.getChannelExtra();
            if (StrUtil.isNotBlank(extraParamsJson)) {
                voucherPayParam = JSONUtil.toBean(extraParamsJson, VoucherPayParam.class);
            } else {
                throw new PayFailureException("储值卡支付参数错误");
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("储值卡支付参数错误");
        }

        String cardNo = voucherPayParam.getCardNo();
        Voucher voucher = voucherManager.findByCardNo(cardNo).orElseThrow(()->new PayFailureException("储值卡不存在"));

        // 卡信息校验
        String timeCheck = voucherQueryService.check(voucher);
        if (StrUtil.isNotBlank(timeCheck)) {
            throw new PayFailureException(timeCheck);
        }
        // 金额是否满足

        return voucher;
    }

    /**
     * 支付前冻结余额 (异步组合支付环境下)
     */
    public VoucherRecord freezeBalance(Integer amount, PayOrder payOrder, Voucher voucher){
        // 待支付余额为零, 不在处理后面的储值卡
        Integer balance = voucher.getBalance();
        // 待支付额大于储值卡余额
        if (amount > balance) {
            throw new PayFailureException("待支付额大于储值卡余额");
        }

        // 冻结待支付的金额
        voucher.setFreezeBalance(amount);

        // 日志
        VoucherLog voucherLog = new VoucherLog()
                .setPaymentId(payOrder.getId())
                .setBusinessId(payOrder.getBusinessNo())
                .setType(VoucherCode.LOG_FREEZE_BALANCE)
                .setRemark(String.format("预冻结金额 %d ", amount))
                .setVoucherId(voucher.getId())
                .setVoucherNo(voucher.getCardNo())
                .setAmount(amount);

        voucherManager.updateById(voucher);
        voucherLogManager.save(voucherLog);

        return new VoucherRecord()
                .setCardNo(voucher.getCardNo())
                .setAmount(amount);
    }

    /**
     * 支付成功, 对冻结的金额进行扣款 (异步组合支付环境下)
     */
    public void paySuccess(Long paymentId){
        voucherPaymentManager.findByPaymentId(paymentId).ifPresent(voucherPayment -> {
            VoucherRecord voucherRecord = voucherPayment.getVoucherRecord();

            Voucher voucher = voucherManager.findByCardNo(voucherRecord.getCardNo())
                    .orElseThrow(() -> new PayFailureException("储值卡不存在"));
            // 余额扣减
            voucher.setBalance(voucher.getBalance()-(voucher.getFreezeBalance()))
                    .setFreezeBalance(0);
            // 日志
            VoucherLog voucherLog = new VoucherLog()
                    .setPaymentId(voucherPayment.getPaymentId())
                    .setBusinessId(voucherPayment.getBusinessNo())
                    .setType(VoucherCode.LOG_REDUCE_AND_UNFREEZE_BALANCE)
                    .setRemark(String.format("扣款金额 %d ", voucherPayment.getAmount()))
                    .setVoucherId(voucher.getId())
                    .setAmount(voucher.getFreezeBalance())
                    .setVoucherNo(voucher.getCardNo());
            voucherManager.updateById(voucher);
            voucherLogManager.save(voucherLog);
        });
    }

    /**
     * 直接支付 (同步支付方式下)
     */
    @Transactional(rollbackFor = Exception.class)
    public VoucherRecord pay(Integer amount, PayOrder payment, Voucher voucher) {
        Integer balance = voucher.getBalance();
        if (amount > balance) {
            throw new PayFailureException("储值卡金额不足");
        }

        // 记录当前卡扣了多少钱 储值卡余额-待支付额
        Integer amountRecord = balance - amount;
        voucher.setBalance(balance-(amount));
        // 日志
        VoucherLog voucherLog = new VoucherLog()
                .setPaymentId(payment.getId())
                .setBusinessId(payment.getBusinessNo())
                .setType(VoucherCode.LOG_PAY)
                .setRemark(String.valueOf(amount))
                .setVoucherId(voucher.getId())
                .setVoucherNo(voucher.getCardNo())
                .setAmount(amount);
        VoucherRecord voucherRecord = new VoucherRecord()
                .setCardNo(voucher.getCardNo())
                .setAmount(amountRecord);
        voucherManager.updateById(voucher);
        voucherLogManager.save(voucherLog);
        return voucherRecord;
    }


    /**
     * 取消支付,解除冻结的额度, 只有在异步支付中才会发生取消操作
     */
    public void close(Long paymentId) {
        voucherPaymentManager.findByPaymentId(paymentId).ifPresent(voucherPayment -> {
            String cardNo = voucherPayment.getVoucherRecord().getCardNo();
            Voucher voucher = voucherManager.findByCardNo(cardNo)
                    .orElseThrow(() -> new PayFailureException("储值卡不存在"));
            // 解冻额度和记录日志
            VoucherLog log = new VoucherLog().setAmount(voucher.getFreezeBalance())
                    .setPaymentId(paymentId)
                    .setBusinessId(voucherPayment.getBusinessNo())
                    .setVoucherId(voucher.getId())
                    .setRemark(String.format("取消支付金额 %d ", voucher.getFreezeBalance()))
                    .setVoucherNo(voucher.getCardNo())
                    .setType(VoucherCode.LOG_CLOSE_PAY);
            voucherManager.updateById(voucher);
            voucherLogManager.save(log);
        });
    }

    /**
     * 退款
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund(Long paymentId, Integer amount) {
        VoucherPayOrder voucherPayOrder = voucherPaymentManager.findByPaymentId(paymentId)
                .orElseThrow(() -> new PayFailureException("储值卡支付记录不存在"));

        // 退回到原有卡中
        this.refundToRaw(voucherPayOrder);
    }

    /**
     * 退款到原有的卡中
     */
    private void refundToRaw(VoucherPayOrder voucherPayOrder){
        int voucherAmount = voucherPayOrder.getAmount();
        // 获取储值卡扣款信息
        VoucherRecord voucherRecord = voucherPayOrder.getVoucherRecord();
        Voucher voucher = voucherManager.findByCardNo(voucherRecord.getCardNo())
                .orElseThrow(() -> new PayFailureException("储值卡不存在"));

        // 退款 和 记录日志
        voucher.setBalance(voucherAmount + voucher.getBalance());

        VoucherLog voucherLog = new VoucherLog()
                .setType(VoucherCode.LOG_REFUND_SELF)
                .setVoucherId(voucher.getId())
                .setVoucherNo(voucher.getCardNo())
                .setAmount(voucherAmount)
                .setPaymentId(voucherPayOrder.getPaymentId())
                .setBusinessId(voucherPayOrder.getStatus())
                .setRemark(String.format("退款金额 %d ", voucherAmount));
        voucherManager.updateById(voucher);
        voucherLogManager.save(voucherLog);
    }

}
