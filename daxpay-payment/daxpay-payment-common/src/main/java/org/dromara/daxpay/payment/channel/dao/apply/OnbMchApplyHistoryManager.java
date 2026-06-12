package org.dromara.daxpay.payment.channel.dao.apply;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.channel.entity.apply.OnbMchApplyHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbMchApplyHistoryManager extends BaseManager<OnbMchApplyHistoryMapper, OnbMchApplyHistory> {
}
