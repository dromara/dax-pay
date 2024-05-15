package cn.bootx.platform.starter.data.perm.dbencrypt;

import cn.bootx.platform.starter.data.perm.configuration.DataPermProperties;
import cn.bootx.platform.common.core.annotation.EncryptionField;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * 数据字段解密插件
 *
 * @author xxm
 * @since 2021/11/23
 */
@Slf4j
@Intercepts(@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }))
@Component
@RequiredArgsConstructor
public class EncryptInterceptor implements Interceptor {

    private final DataPermProperties dataPermProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (dataPermProperties.isEnableFieldDecrypt() && args.length < 2) {
            return invocation.proceed();
        }
        // 获取第二个参数
        Object arg = args[1];
        if (arg instanceof Map) {
            Map<String, ?> map = (Map<String, Object>) arg;
            if (map.containsKey("et")) {
                Object object = map.get("et");
                this.encrypt(object);
            }
        }
        else {
            this.encrypt(arg);
        }
        return invocation.proceed();
    }

    /**
     * 加密
     */
    public void encrypt(Object object) {
        if (Objects.isNull(object)) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(object.getClass(),
                field -> Objects.nonNull(field.getAnnotation(EncryptionField.class)));
        for (Field field : fields) {
            Object fieldValue = this.encryptValue(ReflectUtil.getFieldValue(object, field));
            ReflectUtil.setFieldValue(object, field, fieldValue);
        }

    }

    /**
     * 对象值加密
     */
    public Object encryptValue(Object fieldValue) {
        if (fieldValue instanceof String) {
            AES aes = SecureUtil.aes(dataPermProperties.getFieldDecryptKey().getBytes(StandardCharsets.UTF_8));
            return aes.encryptBase64((String) fieldValue);
        }
        else {
            return fieldValue;
        }
    }

}
