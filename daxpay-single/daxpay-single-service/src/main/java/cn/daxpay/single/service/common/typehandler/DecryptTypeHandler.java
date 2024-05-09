package cn.daxpay.single.service.common.typehandler;

import cn.bootx.platform.starter.data.perm.configuration.DataPermProperties;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Setter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 字段加密处理类
 * @author xxm
 * @since 2024/5/7
 */
public class DecryptTypeHandler extends BaseTypeHandler<String> {

    @Setter
    private static DataPermProperties dataPermProperties;


    /**
     * 加密
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.encryptValue(parameter));
    }

    /**
     * 解密
     */
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return StrUtil.isBlank(columnValue) ? columnValue : this.decryptValue(columnValue);
    }

    /**
     * 解密
     */
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return StrUtil.isBlank(columnValue) ? columnValue : this.decryptValue(columnValue);
    }

    /**
     * 解密
     */
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return StrUtil.isBlank(columnValue) ? columnValue : this.decryptValue(columnValue);
    }

    /**
     * 对象值加密
     */
    public String encryptValue(String fieldValue) {
        AES aes = SecureUtil.aes(dataPermProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
        return aes.encryptBase64(fieldValue);
    }

    /**
     * 对具体的值进行解码
     */
    public String decryptValue(String fieldValue) {
        AES aes = SecureUtil.aes(dataPermProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
        return new String(aes.decrypt(Base64.decode(fieldValue)), StandardCharsets.UTF_8);
    }


}
