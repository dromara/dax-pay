package cn.daxpay.multi.service.service;

import cn.daxpay.multi.service.dao.merchant.UserMerchantManager;
import cn.daxpay.multi.service.entity.merchant.UserMerchant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 用户商户关联关系
 * @author xxm
 * @since 2024/8/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserMerchantService {

    private final UserMerchantManager userMerchantManager;

    /**
     * 根据用户id查询商户号
     */
    @Cacheable(value = "cache:userMerchant", key = "#userId")
    public String findByUserId(Long userId){
        return userMerchantManager.findByField(UserMerchant::getUserId, userId)
                .map(UserMerchant::getMchNo)
                .orElse(null);

    }
}
