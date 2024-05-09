package cn.bootx.platform.baseapi.handler.mp;

import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mp自动填充值
 *
 * @author xxm
 * @since 2021/7/27
 */
@Component
public class MpMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CommonCode.CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, CommonCode.CREATOR, this::getUserid, Long.class);
        this.strictInsertFill(metaObject, CommonCode.LAST_MODIFIED_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, CommonCode.LAST_MODIFIER, this::getUserid, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // mp默认策略如果字段有值则不会赋值, 所以要强制设置值一下
        this.setFieldValByName(CommonCode.LAST_MODIFIED_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(CommonCode.LAST_MODIFIER, this.getUserid(), metaObject);
    }

    public Long getUserid() {
        return SecurityUtil.getCurrentUser().map(UserDetail::getId).orElse(DesensitizedUtil.userId());
    }

}
