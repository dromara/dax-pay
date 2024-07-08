package cn.bootx.platform.iam.entity.permission;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.permission.PermPathConvert;
import cn.bootx.platform.iam.result.permission.PermPathResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 权限资源(url请求)
 * @see cn.bootx.platform.core.annotation.RequestPath
 * @author xxm
 * @since 2020/5/10 23:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_perm_path")
public class PermPath extends MpCreateEntity implements ToResult<PermPathResult> {

    /** 上级编码 */
    private String parentCode;

    /** 终端编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String clientCode;

    /** 标识编码(模块、分组标识) */
    private String code = "";

    /** 名称(请求路径、模块、分组名称) */
    private String name;

    /** 叶子节点, 表示为最下级的请求路径权限标识 */
    private boolean leaf;

    /** 请求路径 */
    private String path;

    /**
     * 请求类型, 为全大写单词
     * @see RequestMethod
     */
    private String method;

    @Override
    public PermPathResult toResult() {
        return PermPathConvert.CONVERT.convert(this);
    }

}
