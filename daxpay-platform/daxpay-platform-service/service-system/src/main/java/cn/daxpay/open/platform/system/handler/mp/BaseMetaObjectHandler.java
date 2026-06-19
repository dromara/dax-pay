package cn.daxpay.open.platform.system.handler.mp;

import cn.daxpay.open.platform.common.mybatisplus.function.MetaObjectFill;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 基础信息自动填填充
///
@Component
public class BaseMetaObjectHandler implements MetaObjectFill {

    @Override
    public void insertFill(MetaObject metaObject, MetaObjectHandler metaObjectHandler) {
        metaObjectHandler.strictInsertFill(metaObject, CommonCode.CREATE_TIME, () -> OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.class);
        metaObjectHandler.strictInsertFill(metaObject, CommonCode.CREATOR, this::getUserid, Long.class);
        metaObjectHandler.strictInsertFill(metaObject, CommonCode.LAST_MODIFIED_TIME, () -> OffsetDateTime.now(ZoneOffset.UTC), OffsetDateTime.class);
        metaObjectHandler.strictInsertFill(metaObject, CommonCode.LAST_MODIFIER, this::getUserid, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject, MetaObjectHandler metaObjectHandler) {
        // mp默认策略如果字段有值则不会赋值, 所以要强制设置值一下
        metaObjectHandler.setFieldValByName(CommonCode.LAST_MODIFIED_TIME, OffsetDateTime.now(ZoneOffset.UTC), metaObject);
        metaObjectHandler.setFieldValByName(CommonCode.LAST_MODIFIER, this.getUserid(), metaObject);
    }

    /// 获取用户Id
    public Long getUserid() {
        try {
            return SecurityUtil.getUserIdOrDefaultId();
        } catch (Exception e) {
            return DesensitizedUtil.userId();
        }
    }

}
