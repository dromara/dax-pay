package org.dromara.daxpay.channel.alipay.dao.assist;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.assist.AlipayMccConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 支付宝经营类目
 * @author xxm
 * @since 2024/11/11
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AlipayMccConstManager extends BaseManager<AlipayMccConstMapper, AlipayMccConst> {
}
