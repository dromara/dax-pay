package cn.bootx.platform.starter.data.perm.dbencrypt;

import cn.bootx.platform.starter.data.perm.configuration.DataPermProperties;
import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据字段解密插件
 *
 * @author xxm
 * @since 2021/11/23
 */
@Slf4j
@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }))
@Component
@RequiredArgsConstructor
public class DecryptInterceptor implements Interceptor {

    private final DataPermProperties dataPermProperties;

    /**
     * 拦截返回的结果
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (!dataPermProperties.isEnableFieldDecrypt()) {
            return result;
        }
        return this.decrypt(result);
    }

    /**
     * 解密
     */
    private Object decrypt(Object result) {
        if (result instanceof List<?>) {
            List<?> list = (ArrayList<?>) result;
            for (Object o : list) {
                // 值为空的情况
                if (Objects.isNull(o)) {
                    continue;
                }
                Field[] fields = ReflectUtil.getFields(o.getClass(),
                        field -> Objects.nonNull(field.getAnnotation(EncryptionField.class)));
                for (Field field : fields) {
                    Object fieldValue = this.decryptValue(ReflectUtil.getFieldValue(o, field));
                    ReflectUtil.setFieldValue(o, field, fieldValue);
                }
            }
            return list;
        }
        return result;
    }

    /**
     * 对具体的值进行解码
     */
    public Object decryptValue(Object fieldValue) {

        if (fieldValue instanceof String) {
            AES aes = SecureUtil.aes(dataPermProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
            return new String(aes.decrypt(Base64.decode((String) fieldValue)), StandardCharsets.UTF_8);
        } else {
            return fieldValue;
        }
    }

}
