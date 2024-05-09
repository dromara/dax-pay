package cn.bootx.platform.iam.core.security.platform.entity;

import cn.bootx.table.modify.annotation.DbComment;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 平台安全策略
 * @author xxm
 * @since 2023/8/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_platform_security_config")
public class PlatformSecurityConfig extends MpBaseEntity {

    /** 开启全局数据权限 */
    @DbComment("开启全局数据权限")
    private Boolean globalDataPerm;

    /** 全局重放过滤 */
    @DbComment("全局重放过滤")
    private Boolean globalReplay;

    /** 全局水印 */
    @DbComment("全局水印")
    private Boolean watermark;
}
