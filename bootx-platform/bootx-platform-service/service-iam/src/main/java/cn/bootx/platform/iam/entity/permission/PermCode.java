package cn.bootx.platform.iam.entity.permission;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.permission.PermCodeConvert;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 权限码
 * @author xxm
 * @since 2024/7/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_perm_code")
public class PermCode extends MpBaseEntity implements ToResult<PermCodeResult> {

    /** 权限码 */
    private String code;

    /** 名称 */
    private String name;

    @Override
    public PermCodeResult toResult() {
        return PermCodeConvert.CONVERT.convert(this);
    }
}
