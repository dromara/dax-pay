package cn.daxpay.open.channel.wechat.convert.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppParam;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvMchAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/// # 微信服务商通道商户应用转换
///
/// MapStruct转换器,负责微信服务商通道商户应用在实体、参数和返回结果之间的相互转换。
///
@Mapper
public interface WechatIsvMchAppConvert {

    WechatIsvMchAppConvert CONVERT = Mappers.getMapper(WechatIsvMchAppConvert.class);

    /// 转换为返回对象
    WechatIsvMchAppResult toResult(WechatIsvMchApp entity);

    /// 转换为实体
    WechatIsvMchApp toEntity(WechatIsvMchAppParam param);

    /// 更新源数据到实体(忽略空值)
    void copy(WechatIsvMchAppParam param, @MappingTarget WechatIsvMchApp entity);
}
