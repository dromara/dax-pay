package cn.bootx.platform.daxpay.service.core.channel.alipay.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/2/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AliPayRecordManager extends BaseManager<AliPayRecordMapper, AliPayRecord> {
}
