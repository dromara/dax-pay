package org.dromara.daxpay.payment.channel.service.apply;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.channel.bo.profile.*;
import org.dromara.daxpay.payment.channel.convert.profile.*;
import org.dromara.daxpay.payment.channel.dao.apply.OnbMchApplyManager;
import org.dromara.daxpay.payment.channel.dao.profile.*;
import org.dromara.daxpay.payment.channel.entity.profile.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 进件资料信息统一服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class OnbProfileService {

    private final OnbBankCardProfileManager bankCardProfileManager;
    private final OnbCardHolderProfileManager cardHolderProfileManager;
    private final OnbLegalProfileManager legalProfileManager;
    private final OnbLicenseProfileManager licenseProfileManager;
    private final OnbShopProfileManager shopProfileManager;
    private final OnbMerchantProfileManager merchantProfileManager;
    private final OnbMchApplyManager onbMchApplyManager;

    // ==================== 银行账户信息相关方法 ====================

    /// 根据申请ID查询银行账户信息
    public OnbBankCardProfileBo findBankCardByApplyId(Long applyId) {
        var opt = bankCardProfileManager.findByApplyId(applyId);
        if (opt.isEmpty()){
            var onbMchApply = onbMchApplyManager.findById(applyId)
                    .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbApplyNotExist"));
            OnbBankCardProfile profile = new OnbBankCardProfile();
            profile.setApplyId(onbMchApply.getId())
                    .setMchNo(onbMchApply.getMchNo())
                    
                    ;
            bankCardProfileManager.save(profile);
            return profile.toResult();
        }
        return opt.get().toResult();
    }

    /// 更新银行账户信息
    @Transactional(rollbackFor = Exception.class)
    public void updateBankCard(OnbBankCardProfileBo param) {
        OnbBankCardProfile entity = bankCardProfileManager.findByApplyId(param.getApplyId())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbBankAccountNotExist"));
        OnbBankCardProfileConvert.CONVERT.copy(param, entity);
        bankCardProfileManager.updateById(entity);
    }

    // ==================== 持卡人信息相关方法 ====================

    /// 根据申请ID查询持卡人信息
    public OnbCardHolderProfileBo findCardHolderByApplyId(Long applyId) {
        var opt = cardHolderProfileManager.findByApplyId(applyId);
        if (opt.isEmpty()){
            var onbMchApply = onbMchApplyManager.findById(applyId)
                    .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbApplyNotExist"));
            OnbCardHolderProfile profile = new OnbCardHolderProfile();
            profile.setApplyId(onbMchApply.getId())
                    .setMchNo(onbMchApply.getMchNo())
                    
                    ;
            cardHolderProfileManager.save(profile);
            return OnbCardHolderProfileConvert.CONVERT.toResult(profile);
        }
        return OnbCardHolderProfileConvert.CONVERT.toResult(opt.get());
    }

    /// 更新持卡人信息
    @Transactional(rollbackFor = Exception.class)
    public void updateCardHolder(OnbCardHolderProfileBo param) {
        OnbCardHolderProfile entity = cardHolderProfileManager.findByApplyId(param.getApplyId())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbCardHolderNotExist"));
        OnbCardHolderProfileConvert.CONVERT.copy(param, entity);
        cardHolderProfileManager.updateById(entity);
    }

    // ==================== 法人信息相关方法 ====================

    /// 根据申请ID查询法人信息
    public OnbLegalProfileBo findLegalByApplyId(Long applyId) {
        var opt = legalProfileManager.findByApplyId(applyId);
        if (opt.isEmpty()){
            var onbMchApply = onbMchApplyManager.findById(applyId)
                    .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbApplyNotExist"));
            OnbLegalProfile profile = new OnbLegalProfile();
            profile.setApplyId(onbMchApply.getId())
                    .setMchNo(onbMchApply.getMchNo())
                    
                    ;
            legalProfileManager.save(profile);
            return OnbLegalProfileConvert.CONVERT.toResult(profile);
        }
        return OnbLegalProfileConvert.CONVERT.toResult(opt.get());
    }

    /// 更新法人信息
    @Transactional(rollbackFor = Exception.class)
    public void updateLegal(OnbLegalProfileBo param) {
        OnbLegalProfile entity = legalProfileManager.findByApplyId(param.getApplyId())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbLegalPersonNotExist"));
        OnbLegalProfileConvert.CONVERT.copy(param, entity);
        legalProfileManager.updateById(entity);
    }

    // ==================== 营业执照信息相关方法 ====================

    /// 根据申请ID查询营业执照信息
    public OnbLicenseProfileBo findLicenseByApplyId(Long applyId) {
        var opt = licenseProfileManager.findByApplyId(applyId);
        if (opt.isEmpty()){
            var onbMchApply = onbMchApplyManager.findById(applyId)
                    .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbApplyNotExist"));
            OnbLicenseProfile profile = new OnbLicenseProfile();
            profile.setApplyId(onbMchApply.getId())
                    .setMchNo(onbMchApply.getMchNo())
                    
                    ;
            licenseProfileManager.save(profile);
            return OnbLicenseProfileConvert.CONVERT.toResult(profile);
        }
        return OnbLicenseProfileConvert.CONVERT.toResult(opt.get());
    }

    /// 更新营业执照信息
    @Transactional(rollbackFor = Exception.class)
    public void updateLicense(OnbLicenseProfileBo param) {
        OnbLicenseProfile entity = licenseProfileManager.findByApplyId(param.getApplyId())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbLicenseNotExist"));
        OnbLicenseProfileConvert.CONVERT.copy(param, entity);
        licenseProfileManager.updateById(entity);
    }

    // ==================== 商户信息相关方法 ====================

    /// 根据申请ID查询商户信息
    public OnbBaseProfileBo findMerchantByApplyId(Long applyId) {
        var opt = merchantProfileManager.findByApplyId(applyId);
        if (opt.isEmpty()){
            var onbMchApply = onbMchApplyManager.findById(applyId)
                    .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbApplyNotExist"));
            OnbBaseProfile profile = new OnbBaseProfile();
            profile.setApplyId(onbMchApply.getId())
                    .setMchNo(onbMchApply.getMchNo())
                    
                    ;
            merchantProfileManager.save(profile);
            return OnbMerchantProfileConvert.CONVERT.toResult(profile);
        }
        return OnbMerchantProfileConvert.CONVERT.toResult(opt.get());
    }

    /// 更新商户信息
    @Transactional(rollbackFor = Exception.class)
    public void updateMerchant(OnbBaseProfileBo param) {
        OnbBaseProfile entity = merchantProfileManager.findByApplyId(param.getApplyId())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbMerchantProfileNotExist"));
        OnbMerchantProfileConvert.CONVERT.copy(param, entity);
        merchantProfileManager.updateById(entity);
    }

    // ==================== 门店信息相关方法 ====================

    /// 根据申请ID查询门店信息
    public OnbShopProfileBo findShopByApplyId(Long applyId) {
        var optional = shopProfileManager.findByApplyId(applyId);
        if (optional.isEmpty()){
            var onbMchApply = onbMchApplyManager.findById(applyId)
                    .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbApplyNotExist"));
            var entity = new OnbShopProfile();
            entity.setApplyId(onbMchApply.getId())
                    .setMchNo(onbMchApply.getMchNo())
                    
                    ;
            shopProfileManager.save(entity);
            return OnbShopProfileConvert.CONVERT.toResult(entity);
        }
        return OnbShopProfileConvert.CONVERT.toResult(optional.get());
    }

    /// 更新门店信息
    @Transactional(rollbackFor = Exception.class)
    public void updateShop(OnbShopProfileBo param) {
        var entity = shopProfileManager.findByApplyId(param.getApplyId())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.onbStoreNotExist"));
        OnbShopProfileConvert.CONVERT.copy(param, entity);
        shopProfileManager.updateById(entity);
    }
}
