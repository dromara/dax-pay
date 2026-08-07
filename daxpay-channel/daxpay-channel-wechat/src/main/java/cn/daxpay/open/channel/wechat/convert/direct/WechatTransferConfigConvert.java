package cn.daxpay.open.channel.wechat.convert.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatTransferConfig;
import cn.daxpay.open.channel.wechat.param.direct.WechatTransferConfigParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatTransferConfigResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 微信转账配置转换
///
/// MapStruct 转换器, 负责转账配置在实体、参数和返回结果之间的转换, 更新时空值不覆盖。
/// 冗余展示字段(发起应用名/wxAppId/场景名)由 Service 填充, 不经 Convert。
///
@Mapper
public interface WechatTransferConfigConvert {

    WechatTransferConfigConvert CONVERT = Mappers.getMapper(WechatTransferConfigConvert.class);

    /// 转换为返回对象
    WechatTransferConfigResult toResult(WechatTransferConfig entity);

    /// 转换为实体
    WechatTransferConfig toEntity(WechatTransferConfigParam param);

    /// 更新源数据到实体(空值不覆盖)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(WechatTransferConfigParam param, @MappingTarget WechatTransferConfig entity);
}
