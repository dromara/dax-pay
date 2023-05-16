package cn.bootx.platform.daxpay.core.paymodel.voucher.service;

import cn.bootx.platform.daxpay.core.paymodel.voucher.dao.VoucherLogManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 储值卡日志
 *
 * @author xxm
 * @date 2022/3/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherLogService {

    private final VoucherLogManager voucherLogManager;

}
