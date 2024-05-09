package cn.bootx.platform.starter.code.gen.code;

import cn.bootx.platform.common.core.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 数据库字段类型与java类型映射
 *
 * @author xxm
 * @since 2022/2/17
 */
@Getter
@AllArgsConstructor
public enum CodeGenColumnTypeEnum {

    TINYINT("tinyint", "Integer", "number"), SMALLINT("smallint", "Integer", "number"),
    MEDIUMINT("mediumint", "Integer", "number"), INT("int", "Integer", "number"),
    INTEGER("integer", "Integer", "number"), BIGINT("bigint", "Long", "string"), FLOAT("float", "Float", "number"),
    DOUBLE("double", "Double", "number"), DECIMAL("decimal", "BigDecimal", "number"), BIT("bit", "Boolean", "boolean"),
    CHAR("char", "String", "string"), VARCHAR("varchar", "String", "string"),
    VARBINARY("VARBINARY", "byte[]", "unknown"), TINYTEXT("tinytext", "String", "string"),
    TEXT("text", "String", "string"), MEDIUMTEXT("mediumtext", "String", "string"),
    LONGTEXT("longtext", "String", "string"), DATE("date", "LocalDate", "string"),
    DATETIME("datetime", "LocalDateTime", "string"), TIME("time", "LocalTime", "string"),
    TIMESTAMP("timestamp", "LocalDateTime", "string");

    private final String columnType;

    private final String javaType;

    private final String tsType;

    /**
     * 数据库类型转换成java类型
     */
    public static String convertJavaType(String columnType) {
        return Arrays.stream(CodeGenColumnTypeEnum.values())
            .filter(e -> Objects.equals(columnType, e.getColumnType()))
            .findFirst()
            .orElseThrow(() -> new BizException("不支持的数据库字段类型"))
            .getJavaType();
    }

    /**
     * 数据库类型转换成TS类型
     */
    public static String convertTsType(String columnType) {
        return Arrays.stream(CodeGenColumnTypeEnum.values())
            .filter(e -> Objects.equals(columnType, e.getColumnType()))
            .findFirst()
            .orElseThrow(() -> new BizException("不支持的数据库字段类型"))
            .getTsType();
    }

}
