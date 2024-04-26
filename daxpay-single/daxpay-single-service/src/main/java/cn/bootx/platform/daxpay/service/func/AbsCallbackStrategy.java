package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.code.PayCallbackStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.payment.callback.service.PayCallbackService;
import cn.bootx.platform.daxpay.service.core.payment.callback.service.RefundCallbackService;
import cn.bootx.platform.daxpay.service.core.record.callback.entity.PayCallbackRecord;
import cn.bootx.platform.daxpay.service.core.record.callback.service.PayCallbackRecordService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 回调处理抽象类, 处理支付回调和退款回调
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Deprecated
public abstract class AbsCallbackStrategy implements PayStrategy {
}
