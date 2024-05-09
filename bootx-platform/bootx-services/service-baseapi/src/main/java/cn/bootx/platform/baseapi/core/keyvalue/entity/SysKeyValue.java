package cn.bootx.platform.baseapi.core.keyvalue.entity;

import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.mybatisplus.base.MpDelEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * kv存储
 *
 * @author xxm
 * @since 2022/3/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("base_key_value")
public class SysKeyValue extends MpDelEntity {

    /**
     * key值
     */
    private String key;

    /**
     * value值
     */
    private String value;

    /**
     * 转换成系统的 KayValue 对象
     */
    public KeyValue toKeyValue() {
        return new KeyValue(key, value);
    }

    /**
     * 从 KayValue 转换
     */
    public static SysKeyValue init(KeyValue keyValue) {
        return new SysKeyValue(keyValue.getKey(), keyValue.getValue());
    }

}
