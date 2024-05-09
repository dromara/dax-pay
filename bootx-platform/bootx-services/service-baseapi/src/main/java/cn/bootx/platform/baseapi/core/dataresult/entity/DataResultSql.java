package cn.bootx.platform.baseapi.core.dataresult.entity;

import cn.bootx.platform.baseapi.core.dataresult.convert.DataResultSqlConvert;
import cn.bootx.platform.baseapi.dto.dataresult.QuerySqlDto;
import cn.bootx.platform.baseapi.dto.dataresult.SqlField;
import cn.bootx.platform.baseapi.dto.dataresult.SqlParam;
import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.JacksonRawTypeHandler;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 数据集SQL语句
 *
 * @author xxm
 * @since 2023/3/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@DbTable(comment = "数据集SQL语句")
@Accessors(chain = true)
@TableName(value = "base_data_result_sql", autoResultMap = true)
public class DataResultSql extends MpBaseEntity implements EntityBaseFunction<QuerySqlDto> {

    /** 数据源ID */
    private Long databaseId;

    /** 名称 */
    private String name;

    /** 是否集合 */
    private Boolean isList;

    /** sql语句 */
    @BigField
    private String sql;

    /** SQL查询参数 */
    @BigField
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private List<SqlParam> params;

    /** SQL查询结果字段 */
    @BigField
    @TableField(typeHandler = JacksonRawTypeHandler.class)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private List<SqlField> fields;

    /**
     * 转换
     */
    @Override
    public QuerySqlDto toDto() {
        return DataResultSqlConvert.CONVERT.convert(this);
    }

}
