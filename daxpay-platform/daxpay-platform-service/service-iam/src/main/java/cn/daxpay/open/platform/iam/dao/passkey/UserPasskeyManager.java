package cn.daxpay.open.platform.iam.dao.passkey;

import java.util.List;
import java.util.Optional;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.passkey.UserPasskey;
import org.springframework.stereotype.Repository;

/// # 用户通行密钥 Manager
///
/// 封装通行密钥凭据的数据访问, 按凭据ID/用户ID查询集中在此层, Service 层不直接使用 lambdaQuery。
///
@Repository
public class UserPasskeyManager extends BaseManager<UserPasskeyMapper, UserPasskey> {

    /// 根据凭据ID查询绑定记录(登录断言验证用)
    public Optional<UserPasskey> findByCredentialId(String credentialId) {
        return lambdaQuery()
            .eq(UserPasskey::getCredentialId, credentialId)
            .oneOpt();
    }

    /// 查询用户已绑定的全部通行密钥(按创建时间正序)
    public List<UserPasskey> findByUserId(Long userId) {
        return lambdaQuery()
            .eq(UserPasskey::getUserId, userId)
            .orderByAsc(UserPasskey::getCreateTime)
            .list();
    }

    /// 根据ID与用户ID查询记录(归属校验, 防越权操作他人凭据)
    public Optional<UserPasskey> findByIdAndUserId(Long id, Long userId) {
        return lambdaQuery()
            .eq(UserPasskey::getId, id)
            .eq(UserPasskey::getUserId, userId)
            .oneOpt();
    }
}
