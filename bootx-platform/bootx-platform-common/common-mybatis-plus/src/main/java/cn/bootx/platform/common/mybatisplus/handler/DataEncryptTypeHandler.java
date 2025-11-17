package cn.bootx.platform.common.mybatisplus.handler;

import cn.bootx.platform.common.config.BootxConfigProperties;
import cn.bootx.platform.core.util.SecureAesGcmEncryptor;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据加密类型处理器, 使用 AES-256-GCM 加密
 * @author xxm
 * @since 2025/9/14
 */
@Slf4j
@Component
public class DataEncryptTypeHandler extends BaseTypeHandler<String> {

    private static SecureAesGcmEncryptor encryptor;
    private static boolean enable;

    /**
     * 初始化加密器
     */
    @PostConstruct
    public void init() {
        var configProperties = SpringUtil.getBean(BootxConfigProperties.class);
        var encrypt = configProperties.getEncrypt();
        if (!encrypt.isEnable()) {
            log.info("数据加密处理器未启用");
            return;
        }
        if (encryptor != null) {
            throw new IllegalArgumentException("数据加密处理器已初始化, 不能重复初始化");
        }
        encryptor = new SecureAesGcmEncryptor(encrypt.getEncryptKey());
        enable = true;
        log.info("数据加密处理器初始化成功");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (!enable) {
            ps.setString(i, parameter);
            return;
        }
        if (StrUtil.isBlank(parameter)){
            ps.setString(i, parameter);
        } else{
            String encrypted = encryptor.encrypt(parameter);
            ps.setString(i, encrypted);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (!enable) {
            return rs.getString(columnName);
        }
        String encrypted = rs.getString(columnName);
        if (StrUtil.isBlank(encrypted)){
            return encrypted;
        }
        return encryptor.decrypt(encrypted);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (!enable) {
            return rs.getString(columnIndex);
        }
        String encrypted = rs.getString(columnIndex);
        if (StrUtil.isBlank(encrypted)){
            return encrypted;
        }
        return encryptor.decrypt(encrypted);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (!enable) {
            return cs.getString(columnIndex);
        }
        String encrypted = cs.getString(columnIndex);
        if (StrUtil.isBlank(encrypted)){
            return encrypted;
        }
        return encryptor.decrypt(encrypted);
    }
}
