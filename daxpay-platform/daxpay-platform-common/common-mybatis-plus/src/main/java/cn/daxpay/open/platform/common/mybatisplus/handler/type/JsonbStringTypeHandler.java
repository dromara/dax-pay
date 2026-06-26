package cn.daxpay.open.platform.common.mybatisplus.handler.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/// # PostgreSQL jsonb 字符串类型处理器。
///
/// 适用于 Java 字段使用 {@link String} 保存原始 JSON 文本、数据库列类型为 `jsonb` 的场景。
/// 由于 PostgreSQL 不会将普通 `varchar` 参数自动隐式转换为 `jsonb`，
/// 如果直接使用字符串绑定，会出现“column is of type jsonb but expression is of type character varying”错误。
/// 因此这里在写入时通过 {@link PGobject} 显式声明数据库参数类型为 `jsonb`。
///
/// 读取时保持返回原始 JSON 字符串，不在此处做对象反序列化，交由业务层按具体配置类型自行处理。
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class JsonbStringTypeHandler extends BaseTypeHandler<String> {

    /// 将 JSON 字符串按 PostgreSQL jsonb 类型写入数据库。
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        // 空字符串不是合法 JSON, jsonb 列应存 NULL
        if (parameter == null || parameter.isBlank()) {
            ps.setNull(i, Types.OTHER);
            return;
        }
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        jsonObject.setValue(parameter);
        ps.setObject(i, jsonObject);
    }

    /// 按列名读取原始 JSON 字符串。
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    /// 按列下标读取原始 JSON 字符串。
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    /// 从存储过程结果中读取原始 JSON 字符串。
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}
