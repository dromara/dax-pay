package cn.daxpay.multi.service.common.tenant;

import cn.bootx.platform.common.mybatisplus.function.MetaObjectFill;
import cn.daxpay.multi.core.context.MchTenantContextHolder;
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
        metaObjectHandler.strictInsertFill(metaObject, "mchNo", this::getMchNo, String.class);
    }

    /**
     * 获取商户号
     */
    public String getMchNo() {
        return MchTenantContextHolder.getMchNo();
    }

}
