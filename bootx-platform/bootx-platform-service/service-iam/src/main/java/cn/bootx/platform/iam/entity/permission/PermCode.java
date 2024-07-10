package cn.bootx.platform.iam.entity.permission;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.permission.PermCodeConvert;
import cn.bootx.platform.iam.param.permission.PermCodeParam;
import cn.bootx.platform.iam.result.permission.PermCodeResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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

    /** 父ID */
    private Long pid;

    /** 权限码 */
    private String code;

    /** 名称 */
    private String name;

    /** 备注 */
    private String remark;

    /** 是否为子节点 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private boolean leaf;

    public static PermCode init(PermCodeParam param) {
        return PermCodeConvert.CONVERT.convert(param);
    }

    @Override
    public PermCodeResult toResult() {
        return PermCodeConvert.CONVERT.convert(this);
    }
}
