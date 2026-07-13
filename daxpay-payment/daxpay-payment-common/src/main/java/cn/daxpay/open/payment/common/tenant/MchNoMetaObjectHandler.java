package cn.daxpay.open.payment.common.tenant;

import cn.daxpay.open.platform.common.mybatisplus.function.MetaObjectFill;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.context.TradeActor;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/// # 商户号自动填充（全端 insert）
///
/// 从线程上下文 [PaymentContext] 读取商户号写入实体 `mchNo`。
/// 实体已显式设置非空 mchNo 时跳过；[MchBaseEntity] 且字段空、上下文也无 mchNo 时 fail-closed。
/// 不再填充 appId：appId 是可空、可推导的业务属性，由业务层显式赋值。
@Component
@RequiredArgsConstructor
public class MchNoMetaObjectHandler implements MetaObjectFill {

    private final PaymentContext paymentContext;

    /// 填充商户号
    @Override
    public void insertFill(MetaObject metaObject, MetaObjectHandler metaObjectHandler) {
        if (!metaObject.hasSetter("mchNo")) {
            return;
        }
        Object existing = metaObject.getValue("mchNo");
        if (existing instanceof String s && StrUtil.isNotBlank(s)) {
            // 业务已显式赋值，不覆盖
            return;
        }
        String mchNo = paymentContext.currentActor()
                .map(TradeActor::getMchNo)
                .filter(StrUtil::isNotBlank)
                .orElse(null);
        if (metaObject.getOriginalObject() instanceof MchBaseEntity) {
            if (StrUtil.isBlank(mchNo)) {
                // 商户实体 insert 必须有有效 mchNo，禁止写出空商户号
                throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "pay.error.assist.mchContextMissing");
            }
            metaObjectHandler.strictInsertFill(metaObject, "mchNo", () -> mchNo, String.class);
            return;
        }
        // 非 MchBaseEntity 但含 mchNo 字段（如可空商户绑定）：有上下文则填，无则跳过
        if (StrUtil.isNotBlank(mchNo)) {
            metaObjectHandler.strictInsertFill(metaObject, "mchNo", () -> mchNo, String.class);
        }
    }
}
