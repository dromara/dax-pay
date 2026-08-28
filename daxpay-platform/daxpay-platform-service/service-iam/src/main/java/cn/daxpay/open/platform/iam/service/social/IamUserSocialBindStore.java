package cn.daxpay.open.platform.iam.service.social;

import cn.daxpay.open.platform.iam.result.social.SocialBindResult;
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

/// # 用户第三方账号绑定存储
///
/// 操作 iam_user_social 表, 提供"按平台+openId 查用户/保存绑定/列绑定/解绑"等能力,
/// 被 SocialEndpoint 直接注入使用(同模块, 无需 SPI 抽象).
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IamUserSocialBindStore {

    private final IamUserSocialManager iamUserSocialManager;

    /// 根据平台来源和平台用户标识查询绑定的本地用户ID
    public Optional<Long> findUserIdBySourceAndOpenId(String source, String openId) {
        return iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getSource, source)
            .eq(IamUserSocial::getOpenId, openId)
            .oneOpt()
            .map(IamUserSocial::getUserId);
    }

    /// 根据本地用户ID和平台来源反查平台用户标识(如微信 openId)
    ///
    /// 供通知发送链路使用: 由平台 userId 反查 openId 后投递消息.
    public Optional<String> findOpenIdByUserId(Long userId, String source) {
        return iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getUserId, userId)
            .eq(IamUserSocial::getSource, source)
            .oneOpt()
            .map(IamUserSocial::getOpenId);
    }
    /// 保存绑定关系
    /// @param userId 本地用户ID
    /// @param clientCode 终端编码
    /// @param authUser 平台返回的用户信息
    @Transactional(rollbackFor = Exception.class)
    public void saveBind(Long userId, String clientCode, AuthUser authUser) {
        this.doSaveBind(userId, clientCode, authUser.getSource(), authUser.getUuid(),
                authUser.getNickname(), authUser.getAvatar());
    }

    /// 保存小程序快捷登录绑定关系(无昵称头像)
    /// @param userId 本地用户ID
    /// @param clientCode 终端编码
    /// @param source 平台来源(weChatApplet/alipayApplet/douyinApplet)
    /// @param openId 平台用户唯一标识
    @Transactional(rollbackFor = Exception.class)
    public void saveAppletBind(Long userId, String clientCode, String source, String openId) {
        this.doSaveBind(userId, clientCode, source, openId, null, null);
    }

    /// 绑定保存核心: 该三方账号已被他人绑定则拒绝, 同用户重复绑定幂等更新, 同一用户同一平台只允许绑定一个账号
    private void doSaveBind(Long userId, String clientCode, String source, String openId,
                            String username, String avatar) {
        // 该三方账号已被其他用户绑定
        Optional<IamUserSocial> existed = iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getSource, source)
            .eq(IamUserSocial::getOpenId, openId)
            .oneOpt();
        if (existed.isPresent() && !existed.get().getUserId().equals(userId)) {
            // 社交登录: 该第三方账号已被其他用户绑定
            throw new OperationFailException("error.social.bind.alreadyBoundByOther");
        }
        // 当前用户已绑定该平台(幂等: 已存在则更新; null 字段不参与 UPDATE)
        if (existed.isPresent()) {
            IamUserSocial entity = existed.get();
            entity.setUsername(username)
                .setAvatar(avatar);
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
            .setUsername(username)
            .setAvatar(avatar);
        iamUserSocialManager.save(entity);
    }

    /// 查询指定用户已绑定的所有第三方账号
    public List<SocialBindResult> findBindsByUserId(Long userId) {
        return iamUserSocialManager.lambdaQuery()
            .eq(IamUserSocial::getUserId, userId)
            .list().stream()
            .map(this::toResult)
            .toList();
    }

    /// 解除指定用户的某个平台绑定
    /// @return 是否解绑成功
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
