package cn.bootx.platform.iam.core.permission.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.iam.core.permission.convert.PermConvert;
import cn.bootx.platform.iam.param.permission.PermPathParam;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.iam.dto.permission.PermPathDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 权限资源(url请求)
 *
 * @author xxm
 * @since 2020/5/10 23:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_perm_path")
public class PermPath extends MpBaseEntity implements EntityBaseFunction<PermPathDto> {

    /** 权限标识 */
    private String code;

    /** 权限名称 */
    private String name;

    /** 分组名称 */
    private String groupName;

    /** 请求类型 */
    private String requestType;

    /** 请求路径 */
    private String path;

    /** 启用 */
    private boolean enable;

    /** 是否通过系统生成的权限 */
    private boolean generate;

    /** 描述 */
    private String remark;

    public static PermPath init(PermPathParam param) {
        return PermConvert.CONVERT.convert(param);
    }

    @Override
    public PermPathDto toDto() {
        return PermConvert.CONVERT.convert(this);
    }

}
