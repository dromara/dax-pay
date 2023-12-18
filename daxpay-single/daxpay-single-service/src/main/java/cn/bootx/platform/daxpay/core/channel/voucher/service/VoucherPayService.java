package cn.bootx.platform.daxpay.core.channel.voucher.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.BigDecimalUtil;
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
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private final VoucherQueryService voucherQueryService;

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
            } else {
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
        // 卡信息校验
        String timeCheck = voucherQueryService.check(vouchers);
        if (StrUtil.isNotBlank(timeCheck)) {
            throw new PayFailureException(timeCheck);
        }
        // 金额是否满足
        BigDecimal amount = vouchers.stream().map(Voucher::getBalance).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        if (BigDecimalUtil.compareTo(amount, payWayParam.getAmount()) < 0) {
            throw new PayFailureException("储值卡余额不足");
        }

        return sort(vouchers);
    }

    /**
     * 支付前冻结余额 (异步组合支付环境下)
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
                    .setRemark(String.format("预冻结金额 %.2f ", amount))
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo());

            // 待支付额大于储值卡余额. 储值卡的金额全冻结
            if (BigDecimalUtil.compareTo(amount, balance) == 1) {
                voucher.setFreezeBalance(balance);
                voucherLog.setAmount(balance);
                // 剩余待支付的金额
                amount = amount.subtract(balance);
            }
            else {
                // 待支付额小于或储值卡余额, 冻结待支付的金额
                voucher.setFreezeBalance(amount);
                voucherLog.setAmount(amount);
                // 待支付的金额扣减完毕, 值设置为零
                amount = BigDecimal.ZERO;
            }
            voucherRecords.add(new VoucherRecord().setCardNo(voucher.getCardNo()).setAmount(voucher.getFreezeBalance()));
            voucherLogs.add(voucherLog);
        }
        voucherManager.updateAllById(vouchers);
        voucherLogManager.saveAll(voucherLogs);
        return voucherRecords;
    }

    /**
     * 支付成功, 对冻结的金额进行扣款 (异步组合支付环境下)
     */
    public void paySuccess(Long paymentId){
        voucherPaymentManager.findByPaymentId(paymentId).ifPresent(voucherPayment -> {
            List<VoucherRecord> voucherRecords = voucherPayment.getVoucherRecords();

            List<String> cardNoList = voucherRecords.stream()
                    .map(VoucherRecord::getCardNo)
                    .collect(Collectors.toList());
            List<Voucher> vouchers = voucherManager.findByCardNoList(cardNoList);
            List<VoucherLog> voucherLogs = new ArrayList<>();
            for (Voucher voucher : vouchers) {
                // 余额扣减
                voucher.setBalance(voucher.getBalance().subtract(voucher.getFreezeBalance()))
                        .setFreezeBalance(BigDecimal.ZERO);
                // 日志
                VoucherLog voucherLog = new VoucherLog()
                        .setPaymentId(voucherPayment.getPaymentId())
                        .setBusinessId(voucherPayment.getBusinessId())
                        .setType(VoucherCode.LOG_REDUCE_AND_UNFREEZE_BALANCE)
                        .setRemark(String.format("扣款金额 %.2f ", voucherPayment.getAmount()))
                        .setVoucherId(voucher.getId())
                        .setAmount(voucher.getFreezeBalance())
                        .setVoucherNo(voucher.getCardNo());
                voucherLogs.add(voucherLog);
            }
            voucherManager.updateAllById(vouchers);
            voucherLogManager.saveAll(voucherLogs);
        });
    }

    /**
     * 直接支付 (同步支付方式下)
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
                    .setRemark(String.format("支付金额 %.2f ", amount))
                    .setVoucherId(voucher.getId())
                    .setVoucherNo(voucher.getCardNo());

            // 记录当前卡扣了多少钱
            BigDecimal amountRecord;
            // 待支付额大于储值卡余额. 储值卡金额全扣光
            if (BigDecimalUtil.compareTo(amount, balance) == 1) {
                amountRecord = voucher.getBalance();
                voucher.setBalance(BigDecimal.ZERO);
                voucherLog.setAmount(balance);
                amount = amount.subtract(balance);
            }
            else {
                // 待支付额小于或储值卡余额, 储值卡余额-待支付额
                amountRecord = balance.subtract(amount);
                voucher.setBalance(balance.subtract(amount));
                voucherLog.setAmount(amount);
                // 支付完毕, 待支付金额归零
                amount = BigDecimal.ZERO;
            }
            voucherLogs.add(voucherLog);
            voucherRecords.add(new VoucherRecord().setCardNo(voucher.getCardNo()).setAmount(amountRecord));
        }
        voucherManager.updateAllById(vouchers);
        voucherLogManager.saveAll(voucherLogs);
        return voucherRecords;
    }


    /**
     * 取消支付,解除冻结的额度, 只有在异步支付中才会发生取消操作
     */
    public void close(Long paymentId) {
        voucherPaymentManager.findByPaymentId(paymentId).ifPresent(voucherPayment -> {
            List<String> cardNoList = voucherPayment.getVoucherRecords().stream()
                    .map(VoucherRecord::getCardNo)
                    .collect(Collectors.toList());
            List<Voucher> vouchers = voucherManager.findByCardNoList(cardNoList);
            // 解冻额度和记录日志
            List<VoucherLog> logs = new ArrayList<>();
            for (Voucher voucher : vouchers) {
                VoucherLog log = new VoucherLog().setAmount(voucher.getFreezeBalance())
                        .setPaymentId(paymentId)
                        .setBusinessId(voucherPayment.getBusinessId())
                        .setVoucherId(voucher.getId())
                        .setRemark(String.format("取消支付金额 %.2f ", voucher.getFreezeBalance()))
                        .setVoucherNo(voucher.getCardNo())
                        .setType(VoucherCode.LOG_CLOSE_PAY);
                logs.add(log);
            }
            voucherManager.updateAllById(vouchers);
            voucherLogManager.saveAll(logs);
        });
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
        // 如果未传入卡号, 默认退到最抗用的一张卡上
        if (StrUtil.isBlank(refundVoucherNo)){
            List<Voucher> sort = this.sort(vouchers);
            refundVoucherNo = sort.get(sort.size()-1).getCardNo();
        }

        // 筛选出来要进行退款的卡
        String finalRefundVoucherNo = refundVoucherNo;
        Voucher voucher = vouchers.stream()
                .filter(vr -> Objects.equals(vr.getCardNo(), finalRefundVoucherNo))
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
                        .setRemark(String.format("退款金额 %.2f, 退款到卡号: %s 储值卡中", voucherAmount, voucher.getCardNo()));
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
