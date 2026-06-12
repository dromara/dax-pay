package org.dromara.daxpay.platform.common.mybatisplus.handler.type;

import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/// # PostgreSQL jsonb 的 {@link List<Integer>} 类型处理器。
///
@MappedJdbcTypes(JdbcType.OTHER)
public class IntegerListTypeHandler extends BaseTypeHandler<List<Integer>> {

    private static final TypeReference<List<Integer>> TYPE_REFERENCE = new TypeReference<>() {};

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType)
            throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        jsonObject.setValue(toJson(parameter));
        ps.setObject(i, jsonObject);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private List<Integer> parse(String json) throws SQLException {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
            return objectMapper.readValue(json, TYPE_REFERENCE);
        } catch (JacksonException e) {
            throw new SQLException("jsonb转List<Integer>失败", e);
        }
    }

    private String toJson(List<Integer> value) throws SQLException {
        try {
            ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
            return objectMapper.writeValueAsString(value == null ? List.of() : value);
        } catch (JacksonException e) {
            throw new SQLException("List<Integer>转jsonb失败", e);
        }
    }
}