package cn.daxpay.open.platform.common.mybatisplus.handler.encrypt;

import cn.daxpay.open.platform.common.config.encrypt.SecureAesGcmEncryptor;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/// # 数据加密类型处理器, 使用 AES-256-GCM 加密
///
/// 支持多密钥版本，密文格式：v{version}:{encrypted}
/// 加密器由 [DataEncryptTypeHandlerConfiguration] 注入全进程唯一的 [SecureAesGcmEncryptor] Bean。
@Slf4j
public class DataEncryptTypeHandler extends BaseTypeHandler<String> {

    private static volatile SecureAesGcmEncryptor encryptor;
    private static volatile boolean enable;
    private static volatile boolean initialized = false;

    /// 初始化加密器（只能初始化一次）
    public static synchronized void initialize(SecureAesGcmEncryptor encryptor, boolean enable) {
        if (initialized) {
            log.warn("数据加密处理器已初始化，跳过重复初始化");
            return;
        }
        DataEncryptTypeHandler.encryptor = encryptor;
        DataEncryptTypeHandler.enable = enable;
        DataEncryptTypeHandler.initialized = true;
        if (enable && encryptor != null) {
            log.info("数据加密处理器初始化成功，当前密钥版本: v{}", encryptor.getCurrentVersion());
        } else {
            log.info("数据加密处理器未启用");
        }
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
