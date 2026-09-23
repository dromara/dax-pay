package cn.daxpay.open.payment.common.json;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/// # unipay 金额数字序列化器
///
/// 配合 [UnipayAmount] 在字段级覆盖平台全局的 Long→字符串序列化([JavaLongTypeModule]),
/// 将金额按契约以**数字**输出。
/// 签名不受影响: 待签串由扁平化取值的字面量构造, 数字与字符串形态结果一致。
public class UnipayAmountSerializer extends ValueSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        gen.writeNumber(value.longValue());
    }
}
