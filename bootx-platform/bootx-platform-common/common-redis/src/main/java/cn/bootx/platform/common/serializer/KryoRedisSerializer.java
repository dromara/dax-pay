package cn.bootx.platform.common.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Redis的Kryo序列化配置
 * @author xxm
 * @since 2024/7/31
 */
@Slf4j
@Component
@NoArgsConstructor
public class KryoRedisSerializer<T> implements RedisSerializer<T> {

    /**
     * 由于 Kryo 不是线程安全的。每个线程都应该有自己的 Kryo，Input 或 Output 实例。
     * 所以，使用 ThreadLocal 存放 Kryo 对象
     * 这样减少了每次使用都实例化一次 Kryo 的开销又可以保证其线程安全
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 设置循环引用，代价是序列化大小轻微变大
//        kryo.setReferences(true);
        kryo.setReferences(false);
        // 设置序列化时对象是否需要设置对象类型
        kryo.setRegistrationRequired(false);
        // 禁用泛型优化可以提高性能，但代价是序列化大小越大。
//        kryo.setOptimizedGenerics(false);
        // 首先尝试使用无参构造方法，如果尝试失败，再尝试使用不需要调用任何构造方法的后备方案,
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        // 提供向后兼容性和可选的前向兼容性。这意味着可以添加或重命名字段, 代价是序列化大小会明显变大(约1/4)。
//        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        return kryo;
    });

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return EMPTY_BYTE_ARRAY;
        }
        // 序列化缓存区默认4K, 最大10M
        try (Output output = new ByteBufferOutput(4096,10*1024*1024)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            // 对象的 Class 信息一起序列化
            kryo.writeClassAndObject(output, t);
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializationException("Could not write byte[]: " + e.getMessage(), e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Input input = new Input(bais)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            // 通过存储在字节数组中的 Class 信息来确定反序列的类型
            Object object = kryo.readClassAndObject(input);
            //noinspection unchecked
            return (T) object;
        } catch (IOException e) {
            throw new SerializationException("Could not read byte[]: " + e.getMessage(), e);
        }
    }
}
