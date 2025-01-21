package org.dromara.daxpay.service.dao.merchant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.entity.merchant.MerchantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 判断用户是否存在
     */
    public boolean existsByUserId(Long userId){
        return existedByField(MerchantUser::getUserId, userId);
    }
    /**
     * 判断用户是否存在
     */
    public boolean existsByUserIds(List<Long> userIds){
        Long count = lambdaQuery().in(MerchantUser::getUserId, userIds)
                .count();
        return userIds.size() == count;
    }


}
