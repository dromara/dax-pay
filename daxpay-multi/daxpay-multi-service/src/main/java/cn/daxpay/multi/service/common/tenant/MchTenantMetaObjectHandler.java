package cn.daxpay.multi.service.common.tenant;

import cn.bootx.platform.common.mybatisplus.function.MetaObjectFill;
import cn.daxpay.multi.service.common.context.MchAppLocal;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 商户信息自动填充
 * @author xxm
 * @since 2024/6/26
 */
@Component
public class MchTenantMetaObjectHandler implements MetaObjectFill {

    /**
     * 填充商户号
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject, MetaObjectHandler metaObjectHandler) {
        // 商户号
        metaObjectHandler.strictInsertFill(metaObject, "mchNo", this::getMchNo, String.class);
        // 应用号
        metaObjectHandler.strictInsertFill(metaObject, "appId", this::getAppId, String.class);
    }

    /**
     * 获取商户号
     */
    public String getMchNo() {
        return MchContextLocal.getMchNo();
    }

    /**
     * 获取应用号
     * 不是所有情况下都会获取到应用号
     */
    public String getAppId() {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        return mchAppInfo.getAppId();
    }

}
