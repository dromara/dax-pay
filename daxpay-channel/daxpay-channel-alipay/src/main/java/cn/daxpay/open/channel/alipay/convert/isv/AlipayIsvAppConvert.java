package cn.daxpay.open.channel.alipay.convert.isv;

import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvApp;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAppParam;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 支付宝服务商应用转换
///
/// MapStruct转换器，负责支付宝服务商应用在实体、参数和返回结果之间的相互转换。
///
@Mapper
public interface AlipayIsvAppConvert {

    AlipayIsvAppConvert CONVERT = Mappers.getMapper(AlipayIsvAppConvert.class);

    /// 转换为返回对象
    AlipayIsvAppResult toResult(AlipayIsvApp entity);

    /// 转换为实体
    AlipayIsvApp toEntity(AlipayIsvAppParam param);

    /// 更新源数据到实体(忽略空值)
    void copy(AlipayIsvAppParam param, @MappingTarget AlipayIsvApp entity);
}
