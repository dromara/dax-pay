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

    /// 统计商户下用户数(存量总数; 运营端商户工作台磁贴计数用)
    ///
    /// 本表为用户与商户的关联关系表, 实体 [MerchantUser] 继承无逻辑删除基类,
    /// 故不存在逻辑删除过滤: 关联行数即用户数, 用户自身停用与否不在本表体现。
    /// 显式按 mchNo 过滤, 不依赖租户自动拼接(运营端无商户上下文)。
    public long countByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(MerchantUser::getMchNo, mchNo)
                .count();
    }

}
