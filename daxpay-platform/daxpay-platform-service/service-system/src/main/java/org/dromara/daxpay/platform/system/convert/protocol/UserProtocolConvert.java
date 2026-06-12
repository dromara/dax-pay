package org.dromara.daxpay.platform.system.convert.protocol;

import org.dromara.daxpay.platform.system.entity.protocol.UserProtocol;
import org.dromara.daxpay.platform.system.entity.protocol.UserProtocolItem;
import org.dromara.daxpay.platform.system.param.protocol.UserProtocolItemParam;
import org.dromara.daxpay.platform.system.param.protocol.UserProtocolParam;
import org.dromara.daxpay.platform.system.result.protocol.UserProtocolItemResult;
import org.dromara.daxpay.platform.system.result.protocol.UserProtocolResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 用户协议管理转换类
///
@Mapper
public interface UserProtocolConvert {
    UserProtocolConvert CONVERT = Mappers.getMapper(UserProtocolConvert.class);

    /// 实体转结果
    ///
    /// @param userProtocol 用户协议实体
    /// @return 用户协议结果
    UserProtocolResult toResult(UserProtocol userProtocol);

    /// 参数转实体
    ///
    /// @param param 用户协议参数
    /// @return 用户协议实体
    UserProtocol toEntity(UserProtocolParam param);

    /// 实体转结果
    ///
    /// @param userProtocolItem 用户协议项实体
    /// @return 用户协议项结果
    UserProtocolItemResult toResult(UserProtocolItem userProtocolItem);

    /// 参数转实体
    ///
    /// @param param 用户协议项参数
    /// @return 用户协议项实体
    UserProtocolItem toEntity(UserProtocolItemParam param);

    /// 复制属性
    ///
    /// @param param 用户协议项参数
    /// @param userProtocolItem 用户协议项实体
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(UserProtocolItemParam param, @MappingTarget UserProtocolItem userProtocolItem);

    /// 复制属性
    ///
    /// @param param 用户协议参数
    /// @param userProtocol 用户协议实体
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(UserProtocolParam param, @MappingTarget UserProtocol userProtocol);
}


