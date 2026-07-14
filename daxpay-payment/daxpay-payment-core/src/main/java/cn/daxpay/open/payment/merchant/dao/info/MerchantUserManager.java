package cn.daxpay.open.payment.merchant.dao.info;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantUserManager extends BaseManager<MerchantUserMapper, MerchantUser> {

    /// 根据用户id查询信息
    public Optional<MerchantUser> findByUserId(Long userId){
        return findByField(MerchantUser::getUserId, userId);
    }

}
