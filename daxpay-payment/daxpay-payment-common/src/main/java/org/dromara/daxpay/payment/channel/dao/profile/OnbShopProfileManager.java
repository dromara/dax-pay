package org.dromara.daxpay.payment.channel.dao.profile;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.channel.entity.profile.OnbShopProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 进件门店信息Manager
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbShopProfileManager extends BaseManager<OnbShopProfileMapper, OnbShopProfile> {

    /// 根据申请ID查询门店信息
    public Optional<OnbShopProfile> findByApplyId(Long applyId) {
        return findByField(OnbShopProfile::getApplyId, applyId);
    }

}
