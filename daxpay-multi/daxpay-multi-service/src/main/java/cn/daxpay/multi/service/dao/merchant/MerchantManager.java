package cn.daxpay.multi.service.dao.merchant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.multi.service.entity.merchant.Merchant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**   
 * 
 * @author xxm  
 * @since 2024/5/27 
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantManager extends BaseManager<MerchantMapper, Merchant> {
}
