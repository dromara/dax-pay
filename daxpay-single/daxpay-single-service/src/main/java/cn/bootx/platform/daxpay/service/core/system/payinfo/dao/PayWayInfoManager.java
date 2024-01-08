package cn.bootx.platform.daxpay.service.core.system.payinfo.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.system.payinfo.entity.PayWayInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayWayInfoManager extends BaseManager<PayWayInfoMapper, PayWayInfo> {
}
