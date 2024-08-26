package cn.daxpay.multi.service.dao.merchant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.merchant.MerchantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
     * 判断商户号是否存在
     */
    public boolean existsByMchNo(String mchNo){
        return existedByField(MerchantUser::getMchNo, mchNo);
    }
}
