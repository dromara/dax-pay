package cn.daxpay.open.platform.iam.convert.twofactor;

import org.mapstruct.Mapper;

/// # 用户双因素认证绑定转换
///
/// 2FA 的 Result 均为聚合/定制构造(状态/初始化/备用码), 不直接映射实体,
/// 故本转换器暂无映射方法, 保留以符合分层规范, 后续可按需补充。
///
@Mapper
public interface UserTwoFactorConvert {

    UserTwoFactorConvert CONVERT = org.mapstruct.factory.Mappers.getMapper(UserTwoFactorConvert.class);
}
