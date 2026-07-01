package cn.daxpay.open.payment.merchant.service.query;

import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.entity.user.UserInfo;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户用户查询服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantUserQueryService {

    private final UserInfoManager userInfoManager;

    /// 判断商户下账号是否存在
    public boolean existsAccountByMchNo(String mchNo, String account) {
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(MerchantUser.class, MerchantUser::getUserId, UserInfo::getId)
                .eq(UserInfo::getAccount, account)
                .eq(MerchantUser::getMchNo, mchNo);
        return userInfoManager.selectJoinCount(wrapper) > 0;
    }

    /// 判断商户下账号是否存在，排除指定用户
    public boolean existsAccountByMchNo(String mchNo, String account, Long excludeUserId) {
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(MerchantUser.class, MerchantUser::getUserId, UserInfo::getId)
                .eq(UserInfo::getAccount, account)
                .eq(MerchantUser::getMchNo, mchNo)
                .ne(UserInfo::getId, excludeUserId);
        return userInfoManager.selectJoinCount(wrapper) > 0;
    }

    /// 判断商户下手机号是否存在
    public boolean existsPhoneByMchNo(String mchNo, String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(MerchantUser.class, MerchantUser::getUserId, UserInfo::getId)
                .eq(UserInfo::getPhone, phone)
                .eq(MerchantUser::getMchNo, mchNo);
        return userInfoManager.selectJoinCount(wrapper) > 0;
    }

    /// 判断商户下手机号是否存在，排除指定用户
    public boolean existsPhoneByMchNo(String mchNo, String phone, Long excludeUserId) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(MerchantUser.class, MerchantUser::getUserId, UserInfo::getId)
                .eq(UserInfo::getPhone, phone)
                .eq(MerchantUser::getMchNo, mchNo)
                .ne(UserInfo::getId, excludeUserId);
        return userInfoManager.selectJoinCount(wrapper) > 0;
    }

    /// 判断商户下邮箱是否存在
    public boolean existsEmailByMchNo(String mchNo, String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(MerchantUser.class, MerchantUser::getUserId, UserInfo::getId)
                .eq(UserInfo::getEmail, email)
                .eq(MerchantUser::getMchNo, mchNo);
        return userInfoManager.selectJoinCount(wrapper) > 0;
    }

    /// 判断商户下邮箱是否存在，排除指定用户
    public boolean existsEmailByMchNo(String mchNo, String email, Long excludeUserId) {
        if (email == null || email.isBlank()) {
            return false;
        }
        MPJLambdaWrapper<UserInfo> wrapper = new MPJLambdaWrapper<>();
        wrapper.innerJoin(MerchantUser.class, MerchantUser::getUserId, UserInfo::getId)
                .eq(UserInfo::getEmail, email)
                .eq(MerchantUser::getMchNo, mchNo)
                .ne(UserInfo::getId, excludeUserId);
        return userInfoManager.selectJoinCount(wrapper) > 0;
    }
}
