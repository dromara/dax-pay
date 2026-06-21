package cn.daxpay.open.platform.iam.service.social;

import cn.daxpay.open.platform.capability.social.bind.SocialBindStore;
import cn.daxpay.open.platform.capability.social.bind.result.SocialBindResult;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;
import cn.daxpay.open.platform.iam.dao.social.IamUserSocialManager;
import cn.daxpay.open.platform.iam.entity.social.IamUserSocial;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/// # 用户第三方账号绑定存储实现
///
/// 实现 capability-social 定义的 SocialBindStore 契约, 操作 iam_user_social 表,
/// 被 SocialEndpoint 通过接口注入使用(无需 capability-social 反向依赖 service-iam)
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IamUserSocialBindStore implements SocialBindStore {

    private final IamUserSocialManager iamUserSocialManager;

    @Override
    public Optional<Long> findUserIdBySourceAndOpenId(String source, String openId) {
        return iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getSource, source)
            .eq(IamUserSocial::getOpenId, openId)
            .oneOpt()
            .map(IamUserSocial::getUserId);
    }

    @Override
    public boolean existsBind(String source, String openId) {
        return iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getSource, source)
            .eq(IamUserSocial::getOpenId, openId)
            .exists();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBind(Long userId, String clientCode, AuthUser authUser) {
        String source = authUser.getSource();
        String openId = authUser.getUuid();
        // 该三方账号已被其他用户绑定
        Optional<IamUserSocial> existed = iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getSource, source)
            .eq(IamUserSocial::getOpenId, openId)
            .oneOpt();
        if (existed.isPresent() && !existed.get().getUserId().equals(userId)) {
            // 社交登录: 该第三方账号已被其他用户绑定
            throw new OperationFailException("error.social.bind.alreadyBoundByOther");
        }
        // 当前用户已绑定该平台(幂等: 已存在则更新)
        if (existed.isPresent()) {
            IamUserSocial entity = existed.get();
            entity.setUsername(authUser.getNickname())
                .setAvatar(authUser.getAvatar());
            iamUserSocialManager.updateById(entity);
            return;
        }
        // 同一用户同一平台只允许绑定一个账号
        boolean userBoundPlatform = iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getUserId, userId)
            .eq(IamUserSocial::getSource, source)
            .exists();
        if (userBoundPlatform) {
            // 社交登录: 您已绑定该平台, 请先解绑
            throw new OperationFailException("error.social.bind.alreadyBoundPlatform");
        }
        IamUserSocial entity = new IamUserSocial()
            .setUserId(userId)
            .setClientCode(clientCode)
            .setSource(source)
            .setOpenId(openId)
            .setUsername(authUser.getNickname())
            .setAvatar(authUser.getAvatar());
        iamUserSocialManager.save(entity);
    }

    @Override
    public List<SocialBindResult> findBindsByUserId(Long userId) {
        return iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getUserId, userId)
            .list().stream()
            .map(this::toResult)
            .toList();
    }

    @Override
    public boolean removeBind(Long userId, String source) {
        return iamUserSocialManager.lambdaUpdate()
            .eq(IamUserSocial::getUserId, userId)
            .eq(IamUserSocial::getSource, source)
            .remove();
    }

    /// 实体转结果
    private SocialBindResult toResult(IamUserSocial entity) {
        return new SocialBindResult()
            .setId(entity.getId())
            .setUserId(entity.getUserId())
            .setClientCode(entity.getClientCode())
            .setSource(entity.getSource())
            .setOpenId(entity.getOpenId())
            .setUsername(entity.getUsername())
            .setAvatar(entity.getAvatar())
            .setCreateTime(entity.getCreateTime());
    }
}
