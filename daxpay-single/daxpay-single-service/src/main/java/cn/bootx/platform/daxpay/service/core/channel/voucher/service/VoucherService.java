package cn.bootx.platform.daxpay.service.core.channel.voucher.service;

import cn.bootx.platform.daxpay.service.code.VoucherCode;
import cn.bootx.platform.daxpay.service.core.channel.voucher.dao.VoucherLogManager;
import cn.bootx.platform.daxpay.service.core.channel.voucher.dao.VoucherManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 储值卡
 *
 * @author xxm
 * @since 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherManager voucherManager;

    private final VoucherLogManager voucherLogManager;


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

}
