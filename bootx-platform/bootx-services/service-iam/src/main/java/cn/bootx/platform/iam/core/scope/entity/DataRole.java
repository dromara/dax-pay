package cn.bootx.platform.iam.core.scope.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.iam.core.scope.convert.DataScopeConvert;
import cn.bootx.platform.iam.dto.scope.DataRoleDto;
import cn.bootx.platform.iam.param.scope.DataRoleParam;
import cn.bootx.platform.starter.data.perm.code.DataScopeEnum;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 数据角色
 *
 * @author xxm
 * @since 2021/12/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_data_role")
public class DataRole extends MpBaseEntity implements EntityBaseFunction<DataRoleDto> {

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /**
     * 类型
     * @see DataScopeEnum
     */
    private String type;

    /** 备注 */
    private String remark;

    public static DataRole init(DataRoleParam in) {
        return DataScopeConvert.CONVERT.convert(in);
    }

    @Override
    public DataRoleDto toDto() {
        return DataScopeConvert.CONVERT.convert(this);
    }

}
