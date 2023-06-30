package cn.bootx.platform.daxpay.core.channel.voucher.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.paymodel.VoucherCode;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherLogManager;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherManager;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherPaymentManager;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherLog;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherPayment;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherRecord;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherPayParam;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherRefundParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * 获取并检查储值卡
     */
    public List<Voucher> getAndCheckVoucher(PayWayParam payWayParam) {
        VoucherPayParam voucherPayParam;
        try {
            // 储值卡参数验证
            String extraParamsJson = payWayParam.getExtraParamsJson();
            if (StrUtil.isNotBlank(extraParamsJson)) {
                voucherPayParam = JSONUtil.toBean(extraParamsJson, VoucherPayParam.class);
            }
            else {
                throw new PayFailureException("储值卡支付参数错误");
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("储值卡支付参数错误");
        }

        List<String> cardNoList = voucherPayParam.getCardNoList();
        List<Voucher> vouchers = voucherManager.findByCardNoList(cardNoList);
        // 判断是否有重复or无效的储值卡
        if (vouchers.size() != cardNoList.size()) {
            throw new PayFailureException("储值卡支付参数错误");
        }
        boolean timeCheck = this.check(vouchers);
        if (!timeCheck) {
            throw new PayFailureException("储值卡不再有效期内");
        }
        // 金额是否满足
        BigDecimal amount = vouchers.stream().map(Voucher::getBalance).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        if (BigDecimalUtil.compareTo(amount, payWayParam.getAmount()) < 0) {
            throw new PayFailureException("储值卡余额不足");
        }

        return sort(vouchers);
    }

    /**
     * 支付前冻结余额
     */
    public List<VoucherRecord> freezeBalance(BigDecimal amount, Payment payment, List<Voucher> vouchers){
        List<VoucherLog> voucherLogs = new ArrayList<>();
        List<VoucherRecord> voucherRecords = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            // 待支付余额为零, 不在处理后面的储值卡
            if (BigDecimalUtil.compareTo(amount, BigDecimal.ZERO) < 1) {
                break;
            }
            BigDecimal balance = voucher.getBalance();
            // 日志
            VoucherLog voucherLog = new VoucherLog()
                    .setPaymentId(payment.getId())
                    .setBusinessId(payment.getBusinessId())
                    .setType(VoucherCode.LOG_FREEZE_BALANCE)
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo());

            // 待支付额大于储值卡余额. 全冻结
            if (BigDecimalUtil.compareTo(amount, balance) == 1) {
                amount = amount.subtract(balance);
                voucher.setFreezeBalance(balance);
                voucherLog.setAmount(balance);
            }
            else {
                amount = BigDecimal.ZERO;
                voucher.setFreezeBalance(amount);
                voucherLog.setAmount(amount);
            }
            voucherRecords.add(new VoucherRecord().setCardNo(voucher.getCardNo()).setAmount(voucher.getFreezeBalance()));
            voucherLogs.add(voucherLog);
        }
        voucherManager.updateAllById(vouchers);
        voucherLogManager.saveAll(voucherLogs);
        return voucherRecords;
    }

    /**
     * 支付成功, 对冻结的金额进行扣款
     */
    public void paySuccess(Long paymentId){
        voucherPaymentManager.findByPaymentId(paymentId).ifPresent(voucherPayment -> {

            List<VoucherLog> voucherLogs = new ArrayList<>();
            for (Voucher voucher : vouchers) {
                // 待支付余额为零, 不在处理后面的储值卡
                if (BigDecimalUtil.compareTo(amount, BigDecimal.ZERO) < 1) {
                    break;
                }
                BigDecimal balance = voucher.getBalance();
                // 日志
                VoucherLog voucherLog = new VoucherLog()
                        .setPaymentId(payment.getId())
                        .setBusinessId(payment.getBusinessId())
                        .setType(VoucherCode.LOG_FREEZE_BALANCE)
                        .setVoucherId(voucher.getId())
                        .setVoucherNo(voucher.getCardNo());

                // 待支付额大于储值卡余额. 全冻结
                if (BigDecimalUtil.compareTo(amount, balance) == 1) {
                    amount = amount.subtract(balance);
                    voucher.setFreezeBalance(balance);
                    voucherLog.setAmount(balance);
                } else {
                    voucher.setFreezeBalance(balance.subtract(amount));
                    voucherLog.setAmount(amount);
                }
                voucherLogs.add(voucherLog);
            }
            voucherManager.updateAllById(vouchers);
            voucherLogManager.saveAll(voucherLogs);
        });
    }

    /**
     * 直接支付
     */
    @Transactional(rollbackFor = Exception.class)
    public List<VoucherRecord> pay(BigDecimal amount, Payment payment, List<Voucher> vouchers) {
        List<VoucherLog> voucherLogs = new ArrayList<>();
        List<VoucherRecord> voucherRecords = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            // 待支付余额为零, 不在处理后面的储值卡
            if (BigDecimalUtil.compareTo(amount, BigDecimal.ZERO) < 1) {
                break;
            }
            BigDecimal balance = voucher.getBalance();
            // 日志
            VoucherLog voucherLog = new VoucherLog()
                    .setPaymentId(payment.getId())
                    .setBusinessId(payment.getBusinessId())
                    .setType(VoucherCode.LOG_PAY)
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo());

            // 记录当前卡扣了多少钱
            BigDecimal amountRecord;
            // 待支付额大于储值卡余额. 全扣光
            if (BigDecimalUtil.compareTo(amount, balance) == 1) {
                amountRecord = voucher.getBalance();
                amount = amount.subtract(balance);
                voucher.setBalance(BigDecimal.ZERO);
                voucherLog.setAmount(balance);
            }
            else {
                amountRecord = balance.subtract(amount);
                amount = BigDecimal.ZERO;
                voucher.setBalance(balance.subtract(amount));
                voucherLog.setAmount(amount);
            }
            voucherLogs.add(voucherLog);
            voucherRecords.add(new VoucherRecord().setCardNo(voucher.getCardNo()).setAmount(amountRecord));
        }
        voucherManager.updateAllById(vouchers);
        voucherLogManager.saveAll(voucherLogs);
        return voucherRecords;
    }


    /**
     * 取消支付, 可配置解除冻结金额
     * @param freeze 是否是冻结模式, 是的话对冻结金额进行解冻, 不是的话返还扣减的金额
     */
    public void close(Long paymentId, boolean freeze) {

        VoucherPayment voucherPayment = voucherPaymentManager.findByPaymentId(paymentId).orElseThrow(DataNotExistException::new);


        // 查找支付记录日志
        List<VoucherLog> voucherLogs = voucherLogManager.findByPaymentIdAndType(paymentId, VoucherCode.LOG_PAY);
        // 查出关联的储值卡
        Map<Long, VoucherLog> voucherLogMap = voucherLogs.stream()
                .collect(Collectors.toMap(VoucherLog::getVoucherId, Function.identity()));
        List<Voucher> vouchers = voucherManager.findAllByIds(voucherLogMap.keySet());
        // 执行退款并记录日志
        List<VoucherLog> logs = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            VoucherLog voucherLog = voucherLogMap.get(voucher.getId());
            voucher.setBalance(voucher.getBalance().add(voucherLog.getAmount()));
            VoucherLog log = new VoucherLog().setAmount(voucherLog.getAmount())
                    .setPaymentId(paymentId)
                    .setBusinessId(voucherLog.getBusinessId())
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo())
                    .setType(VoucherCode.LOG_CLOSE_PAY);
            logs.add(log);
        }
        voucherManager.updateAllById(vouchers);
        voucherLogManager.saveAll(logs);
    }

    /**
     * 退款
     * 全额退款支持分别退回到原有卡和统一退回到某张卡中, 默认退回到原有卡中
     * 部分退款, 会退到指定的一张卡上, 如果不指定, 则自动退到有效期最久的卡上
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund(Long paymentId, BigDecimal amount, VoucherRefundParam voucherRefundParam) {
        VoucherPayment voucherPayment = voucherPaymentManager.findByPaymentId(paymentId)
                .orElseThrow(() -> new PayFailureException("储值卡支付记录不存在"));
        // 全部退款还是部分退款
        if (BigDecimalUtil.compareTo(amount,voucherPayment.getAmount())==0){
            // 是否全部退到一张卡中
            if (voucherRefundParam.isRefundToOne()){
                this.refundToOne(voucherPayment, voucherRefundParam.getRefundVoucherNo());
            } else {
                // 退回到原有卡中
                this.refundToRaw(voucherPayment);
            }
        } else {
            this.refundToOne(voucherPayment, voucherRefundParam.getRefundVoucherNo());
        }
    }

    /**
     * 全部退款到一张卡中
     */
    private void refundToOne(VoucherPayment voucherPayment, String refundVoucherNo){
        // 获取储值卡扣款信息
        List<VoucherRecord> voucherRecords = voucherPayment.getVoucherRecords();
        Map<String, BigDecimal> voucherRecordMap = voucherRecords.stream()
                .collect(Collectors.toMap(VoucherRecord::getCardNo, VoucherRecord::getAmount, CollectorsFunction::retainLatest));
        List<String> cardNoList = voucherRecords.stream()
                .map(VoucherRecord::getCardNo)
                .collect(Collectors.toList());
        List<Voucher> vouchers = voucherManager.findByCardNoList(cardNoList);
        // 筛选出来要进行退款的卡
        Voucher voucher = vouchers.stream()
                .filter(vr -> Objects.equals(vr.getCardNo(), refundVoucherNo))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("退款卡号不存在"));
        // 将金额全部推到指定的卡上
        voucher.setBalance(voucher.getBalance().add(voucherPayment.getAmount()));
        // 记录日志
        List<VoucherLog> voucherLogs = new ArrayList<>();
        for (Voucher v : vouchers) {
            BigDecimal voucherAmount = voucherRecordMap.get(v.getCardNo());
            VoucherLog voucherLog = new VoucherLog()
                    .setType(VoucherCode.LOG_REFUND_SELF)
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo())
                    .setAmount(voucherAmount)
                    .setPaymentId(voucherPayment.getPaymentId())
                    .setBusinessId(voucherPayment.getBusinessId())
                    .setRemark(String.format("退款金额 %.2f, 退款到卡号: %s 储值卡中", voucherAmount, voucher.getCardNo()));
            // 接收退款的卡
            if (Objects.equals(v,voucher)){
                voucherLog.setAmount(voucherPayment.getAmount())
                        .setType(VoucherCode.LOG_REFUND_SELF)
                        .setRemark(String.format("退款金额 %.2f, 退款到卡号: %s 储值卡中", voucherAmount, voucher.getCardNo()))
            }
        }
        voucherManager.updateById(voucher);
        voucherLogManager.saveAll(voucherLogs);
    }

    /**
     * 退款到原有的卡中
     */
    private void refundToRaw(VoucherPayment voucherPayment){
        // 获取储值卡扣款信息
        List<VoucherRecord> voucherRecords = voucherPayment.getVoucherRecords();
        List<String> cardNoList = voucherRecords.stream()
                .map(VoucherRecord::getCardNo)
                .collect(Collectors.toList());
        Map<String, BigDecimal> voucherRecordMap = voucherRecords.stream()
                .collect(Collectors.toMap(VoucherRecord::getCardNo, VoucherRecord::getAmount, CollectorsFunction::retainLatest));
        List<Voucher> vouchers = voucherManager.findByCardNoList(cardNoList);

        // 退款 和 记录日志
        List<VoucherLog> voucherLogs = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            BigDecimal voucherAmount = voucherRecordMap.get(voucher.getCardNo());
            voucher.setBalance(voucherAmount.add(voucher.getBalance()));

            voucherLogs.add(new VoucherLog()
                    .setType(VoucherCode.LOG_REFUND_SELF)
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo())
                    .setAmount(voucherAmount)
                    .setPaymentId(voucherPayment.getPaymentId())
                    .setBusinessId(voucherPayment.getBusinessId())
                    .setRemark(String.format("退款金额 %.2f ", voucherAmount)));
        }
        voucherManager.updateAllById(vouchers);
        voucherLogManager.saveAll(voucherLogs);
    }

    /**
     * 卡信息检查
     */
    private boolean check(List<Voucher> vouchers) {
        // 判断有效期
        return vouchers.stream()
                .filter(voucher -> !Objects.equals(voucher.isEnduring(), true))
                .allMatch(voucher -> LocalDateTimeUtil.between(LocalDateTime.now(), voucher.getStartTime(),
                        voucher.getEndTime()));
    }

    /**
     * 对储值卡进行排序
     * 有期限的在前面, 同样有期限到期时间短的在前面, 同样到期日余额小的在前面, 金额一样id小的前面
     */
    private List<Voucher> sort(List<Voucher> vouchers){
        vouchers.sort(this::compareTime);
        return vouchers;
    }

    /**
     * 比较储值卡的期限
     */
    private int compareTime(Voucher v1,Voucher v2){
        // 期限对比, 都为长期
        if (v1.isEnduring()&&v2.isEnduring()){
            // 比较余额
            return compareBalance(v1,v2);
        }
        // 都不为长期, 且金额一致
        if (Objects.equals(v1.getEndTime(),v2.getEndTime())){
            // 比较余额
            return compareBalance(v1,v2);
        }
        // 期限对比 其中一个为长期
        if (v1.isEnduring()^v2.isEnduring()){
            return v1.isEnduring()?1:-1;
        }
        // 比较期限
        return v1.getEndTime().compareTo(v2.getEndTime());
    }

    /**
     * 比较储值卡的余额, 余额一致比较主键
     */
    private int compareBalance(Voucher v1,Voucher v2) {
        int i = BigDecimalUtil.compareTo(v1.getBalance(), v2.getBalance());
        if (i==0){
            return Long.compare(v1.getId(),v2.getId());
        }
        return i;
    }

}
