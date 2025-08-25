package org.dromara.daxpay.channel.alipay.convert;

import org.dromara.daxpay.channel.alipay.entity.assist.AlipayMccConst;
import org.dromara.daxpay.channel.alipay.result.assist.AlipayMccConstResult;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

/**
 *
 * @author xxm
 * @since 2024/11/11
 */
@Mapper
public interface AlipayMccConstConvert {
    AlipayMccConstConvert INSTANCE = getMapper(AlipayMccConstConvert.class);

    AlipayMccConstResult toResult(AlipayMccConst entity);
}
