package cn.daxpay.multi.merchant.service.merchant;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.service.dao.merchant.MerchantManager;
import cn.daxpay.multi.service.entity.merchant.Merchant;
import cn.daxpay.multi.service.param.merchant.MerchantParam;
import cn.daxpay.multi.service.result.merchant.MerchantResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商户信息管理
 * @author xxm
 * @since 2024/8/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantInfoService {
    private final MerchantManager merchantManager;

    /**
     * 获取商户信息
     */
    public MerchantResult getMerchant() {
        String mchNo = MchContextLocal.getMchNo();
        return merchantManager.findByMchNo(mchNo).map(Merchant::toResult).orElseThrow(() -> new DataNotExistException("商户信息不存在"));
    }

    /**
     * 修改
     */
    public void update(MerchantParam param) {
        String mchNo = MchContextLocal.getMchNo();
        Merchant merchant = merchantManager.findByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("商户信息不存在"));
        BeanUtil.copyProperties(param, merchant, CopyOptions.create().ignoreNullValue());
        merchantManager.updateById(merchant);
    }

}
