package cn.bootx.platform.iam.entity.permission;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.permission.PermPathConvert;
import cn.bootx.platform.iam.param.permission.PermPathParam;
import cn.bootx.platform.iam.result.permission.PermPathResult;
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
public class PermPath extends MpBaseEntity implements ToResult<PermPathResult> {

    /** 请求路径标识 */
    private String code;

    /** 请求路径 */
    private String path;

    /** 功能模块名称 */
    private String moduleName;

    /** 功能分组名称 */
    private String groupName;

    /** 请求路径名称 */
    private String name;

    /** 请求类型 */
    private String requestType;

    /** 启用 */
    private boolean enable;

    /** 是否通过系统生成的权限 */
    private boolean generate;

    /** 描述 */
    private String remark;

    public static PermPath init(PermPathParam param) {
        return PermPathConvert.CONVERT.convert(param);
    }

    @Override
    public PermPathResult toResult() {
        return PermPathConvert.CONVERT.convert(this);
    }

}
