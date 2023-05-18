package cn.bootx.platform.daxpay.core.channel.voucher.service;

import cn.bootx.platform.daxpay.code.paymodel.VoucherCode;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherLogManager;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherManager;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherLog;
import cn.bootx.platform.daxpay.param.paymodel.voucher.VoucherGenerationParam;
import cn.bootx.platform.daxpay.param.paymodel.voucher.VoucherImportParam;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 储值卡
 *
 * @author xxm
 * @date 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherManager voucherManager;

    private final VoucherLogManager voucherLogManager;

    /**
     * 批量生成
     */
    @Transactional(rollbackFor = Exception.class)
    public void generationBatch(VoucherGenerationParam param) {
        Integer count = param.getCount();
        List<Voucher> vouchers = new ArrayList<>(count);
        long batchNo = IdUtil.getSnowflakeNextId();
        for (int i = 0; i < count; i++) {
            Voucher voucher = new Voucher()
                .setCardNo('V' + IdUtil.getSnowflakeNextIdStr() + RandomUtil.randomNumbers(2))
                .setBatchNo(batchNo)
                .setBalance(param.getFaceValue())
                .setFaceValue(param.getFaceValue())
                .setEnduring(param.getEnduring())
                .setStatus(param.getStatus());
            if (Objects.equals(param.getEnduring(), Boolean.FALSE)) {
                voucher.setStartTime(param.getStartTime()).setEndTime(param.getEndTime());
            }
            vouchers.add(voucher);
        }
        voucherManager.saveAll(vouchers);
        // 日志
        List<VoucherLog> voucherLogs = vouchers.stream()
            .map(voucher -> new VoucherLog().setType(VoucherCode.LOG_ACTIVE)
                .setAmount(voucher.getBalance())
                .setVoucherId(voucher.getId())
                .setVoucherNo(voucher.getCardNo()))
            .collect(Collectors.toList());
        voucherLogManager.saveAll(voucherLogs);
    }

    /**
     * 批量导入
     */
    public void importBatch(VoucherImportParam param) {

    }

    /**
     * 启用
     */
    public void unlock(Long id) {
        voucherManager.changeStatus(id, VoucherCode.STATUS_NORMAL);
    }

    /**
     * 冻结
     */
    public void lock(Long id) {
        voucherManager.changeStatus(id, VoucherCode.STATUS_FORBIDDEN);
    }

    /**
     * 批量启用
     */
    public void unlockBatch(List<Long> ids) {
        voucherManager.changeStatusBatch(ids, VoucherCode.STATUS_NORMAL);
    }

    /**
     * 批量冻结
     */
    public void lockBatch(List<Long> ids) {
        voucherManager.changeStatusBatch(ids, VoucherCode.STATUS_FORBIDDEN);
    }

    /**
     * 更改有效期
     */
    public void changeEnduring() {

    }

}
