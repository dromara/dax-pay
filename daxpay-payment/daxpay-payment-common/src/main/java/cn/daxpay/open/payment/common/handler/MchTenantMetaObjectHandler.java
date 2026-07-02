package cn.daxpay.open.payment.common.handler;

import cn.daxpay.open.platform.common.mybatisplus.function.MetaObjectFill;
import cn.daxpay.open.payment.common.runtime.PaymentContext;
import cn.daxpay.open.payment.common.runtime.TradeActor;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/// # 商户信息自动填充
///
/// 从线程上下文 [PaymentContext] 读取商户号;未开启作用域时降级跳过(不抛异常)。
/// 不再填充 appId:appId 是可空、可推导的业务属性,由业务层显式赋值到实体。
@Component
@RequiredArgsConstructor
public class MchTenantMetaObjectHandler implements MetaObjectFill {

    private final PaymentContext paymentContext;

    /// 填充商户号, 如果值为 null 不进行填充
    /// @param metaObject 元对象
    @Override
    public void insertFill(MetaObject metaObject, MetaObjectHandler metaObjectHandler) {
        // 商户号(null-safe,非交易上下文的 insert 降级跳过)
        String mchNo = paymentContext.currentActor().map(TradeActor::getMchNo).orElse(null);
        metaObjectHandler.strictInsertFill(metaObject, "mchNo", () -> mchNo, String.class);
    }

}
