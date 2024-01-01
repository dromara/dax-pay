package cn.bootx.platform.daxpay.core.record.callback.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.record.callback.entity.CallbackRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 支付回调通知
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Repository
public class CallbackRecordManager extends BaseManager<CallbackRecordMapper, CallbackRecord> {
}
