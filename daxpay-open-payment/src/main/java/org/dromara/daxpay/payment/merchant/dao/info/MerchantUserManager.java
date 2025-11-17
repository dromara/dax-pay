package org.dromara.daxpay.payment.merchant.dao.info;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.info.MerchantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/8/22
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantUserManager extends BaseManager<MerchantUserMapper, MerchantUser> {

    /**
     * 根据用户id查询信息
     */
    public Optional<MerchantUser> findByUserId(Long userId){
        return findByField(MerchantUser::getUserId, userId);
    }



}
