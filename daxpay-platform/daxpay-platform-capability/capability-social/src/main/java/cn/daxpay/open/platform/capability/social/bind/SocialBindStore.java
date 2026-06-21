package cn.daxpay.open.platform.capability.social.bind;

import cn.daxpay.open.platform.capability.social.bind.result.SocialBindResult;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;

import java.util.List;
import java.util.Optional;

/// # 社交账号绑定存储接口
///
/// capability-social 层只定义绑定关系的访问契约, 具体实现由 service-iam 提供(操作 iam_user_social 表),
/// 以保证依赖方向: 业务服务层依赖能力层, 而非相反
///
public interface SocialBindStore {

    /// 根据平台来源和平台用户标识查询绑定的本地用户ID
    Optional<Long> findUserIdBySourceAndOpenId(String source, String openId);

    /// 判断指定平台账号是否已被绑定
    boolean existsBind(String source, String openId);

    /// 保存绑定关系
    /// @param userId 本地用户ID
    /// @param clientCode 终端编码
    /// @param authUser 平台返回的用户信息
    void saveBind(Long userId, String clientCode, AuthUser authUser);

    /// 查询指定用户已绑定的所有第三方账号
    List<SocialBindResult> findBindsByUserId(Long userId);

    /// 解除指定用户的某个平台绑定
    /// @return 是否解绑成功
    boolean removeBind(Long userId, String source);
}
