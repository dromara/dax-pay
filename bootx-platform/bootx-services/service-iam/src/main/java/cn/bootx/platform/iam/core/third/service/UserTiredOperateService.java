package cn.bootx.platform.iam.core.third.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.iam.core.third.dao.UserThirdInfoManager;
import cn.bootx.platform.iam.core.third.dao.UserThirdManager;
import cn.bootx.platform.iam.core.third.entity.UserThird;
import cn.bootx.platform.iam.core.third.entity.UserThirdInfo;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 用户绑定操作类
 *
 * @author xxm
 * @since 2022/7/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTiredOperateService {

    private final UserThirdManager userThirdManager;

    private final UserThirdInfoManager userThirdInfoManager;

    private final UserInfoManager userInfoManager;

    /**
     * 判断该OpenId是否已经被使用
     */
    public void checkOpenIdBind(String openId, SFunction<UserThird, String> field) {
        if (userThirdManager.existedByField(field, openId)) {
            throw new BizException("该第三方平台已经被绑定");
        }
    }

    /**
     * 绑定 开放平台id
     */
    public void bindOpenId(Long userId, String openId, BiConsumer<UserThird, String> function) {
        // 没有新增, 存在则更新
        UserThird userThird = userThirdManager.findByUserId(userId).orElse(null);
        if (Objects.isNull(userThird)) {
            UserInfo userInfo = userInfoManager.findById(userId).orElseThrow(UserInfoNotExistsException::new);
            userThird = new UserThird();
            userThird.setUserId(userInfo.getId());
            function.accept(userThird, openId);
            userThirdManager.save(userThird);
        }
        else {
            function.accept(userThird, openId);
            userThirdManager.updateById(userThird);
        }
    }

    /**
     * 绑定开放平台信息
     */
    public void bindOpenInfo(Long userId, AuthUser authUser, String clientCode) {
        UserThirdInfo userThirdInfo = new UserThirdInfo().setUserId(userId)
            .setClientCode(clientCode)
            .setUsername(authUser.getUsername())
            .setNickname(authUser.getNickname())
            .setAvatar(authUser.getAvatar())
            .setThirdUserId(authUser.getUuid());
        this.bindOpenInfo(userThirdInfo);
    }

    /**
     * 绑定开放平台信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindOpenInfo(UserThirdInfo userThirdInfo) {

        userThirdInfoManager.deleteByUserAndClientCode(userThirdInfo.getUserId(), userThirdInfo.getClientCode());

        userThirdInfoManager.save(userThirdInfo);

    }

}
