package cn.bootx.platform.daxpay.service.core.channel.voucher.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.VoucherConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/2/17
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherConfigManager extends BaseManager<VoucherConfigMapper, VoucherConfig> {
}
