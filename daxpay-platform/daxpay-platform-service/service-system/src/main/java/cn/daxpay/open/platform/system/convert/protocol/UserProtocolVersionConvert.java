package cn.daxpay.open.platform.system.convert.protocol;

import cn.daxpay.open.platform.system.entity.protocol.UserProtocolVersion;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolVersionParam;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolVersionResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 用户协议版本转换类
///
@Mapper
public interface UserProtocolVersionConvert {
    UserProtocolVersionConvert CONVERT = Mappers.getMapper(UserProtocolVersionConvert.class);

    /// 实体转结果
    ///
    /// @param entity 用户协议版本实体
    /// @return 用户协议版本结果
    UserProtocolVersionResult toResult(UserProtocolVersion entity);

    /// 参数转实体
    ///
    /// @param param 用户协议版本参数
    /// @return 用户协议版本实体
    UserProtocolVersion toEntity(UserProtocolVersionParam param);

    /// 复制属性
    ///
    /// @param param 用户协议版本参数
    /// @param entity 用户协议版本实体
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(UserProtocolVersionParam param, @MappingTarget UserProtocolVersion entity);
}
