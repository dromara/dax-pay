package cn.daxpay.open.payment.common.handler;

import cn.daxpay.open.platform.common.mybatisplus.function.MetaObjectFill;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/// # 商户信息自动填充
///
@Component
@RequiredArgsConstructor
public class MchTenantMetaObjectHandler implements MetaObjectFill {

    private final PaymentContext apiContext;

    /// 填充商户相关信息, 如果值为null不进行填充
    /// @param metaObject 元对象
    @Override
    public void insertFill(MetaObject metaObject, MetaObjectHandler metaObjectHandler) {
        // 商户号
        metaObjectHandler.strictInsertFill(metaObject, "mchNo", this::getMchNo, String.class);
        // 应用号
        metaObjectHandler.strictInsertFill(metaObject, "appId", this::getAppId, String.class);
    }

    /// 获取商户号
    public String getMchNo() {
        return apiContext.getTradeInfo().getMchNo();
    }

    /// 获取应用号
    /// 不是所有情况下都会获取到应用号
    public String getAppId() {
        return apiContext.getTradeInfo().getAppId();
    }

}

