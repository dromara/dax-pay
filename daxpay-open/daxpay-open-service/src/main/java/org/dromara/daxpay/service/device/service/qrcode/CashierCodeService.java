package org.dromara.daxpay.service.device.service.qrcode;

import cn.bootx.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.core.enums.CashierAmountTypeEnum;
import org.dromara.daxpay.service.common.entity.config.PlatformUrlConfig;
import org.dromara.daxpay.service.common.service.config.PlatformConfigService;
import org.dromara.daxpay.service.device.convert.qrcode.CashierCodeConvert;
import org.dromara.daxpay.service.device.dao.qrcode.config.CashierCodeConfigManager;
import org.dromara.daxpay.service.device.dao.qrcode.info.CashierCodeManager;
import org.dromara.daxpay.service.device.dao.qrcode.template.CashierCodeTemplateManager;
import org.dromara.daxpay.service.device.entity.qrcode.info.CashierCode;
import org.dromara.daxpay.service.device.param.commom.AssignMerchantParam;
import org.dromara.daxpay.service.device.param.qrcode.info.CashierCodeBatchParam;
import org.dromara.daxpay.service.device.param.qrcode.info.CashierCodeCreateParam;
import org.dromara.daxpay.service.device.param.qrcode.info.CashierCodeQuery;
import org.dromara.daxpay.service.device.param.qrcode.info.CashierCodeUpdateParam;
import org.dromara.daxpay.service.device.result.qrcode.info.CashierCodeResult;
import org.dromara.daxpay.service.merchant.dao.app.MchAppManager;
import org.dromara.daxpay.service.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.service.merchant.local.MchContextLocal;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * 收款码牌服务
 * @author xxm
 * @since 2025/7/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodeService {
    private final CashierCodeManager cashierCodeManager;
    private final CashierCodeTemplateManager cashierCodeTemplateManager;
    private final CashierCodeConfigManager cashierCodeConfigManager;
    private final MerchantManager merchantManager;
    private final MchAppManager mchAppManager;
    private final PlatformConfigService platformConfigService;

    /**
     * 分页
     */
    public PageResult<CashierCodeResult> page(PageParam pageParam, CashierCodeQuery query){
        return MpUtil.toPageResult(cashierCodeManager.page(pageParam,query));
    }

    /**
     * 查询详情
     */
    public CashierCodeResult findById(Long id){
        return cashierCodeManager.findById(id).map(CashierCode::toResult)
                .orElseThrow(() -> new DataNotExistException("收银码牌不存在"));
    }

    /**
     * 批量创建空白码牌
     */
    @Transactional(rollbackFor = Exception.class)
    public void createBatch(CashierCodeBatchParam param){
        // 判断批次号是否已存在
        if (cashierCodeManager.existedByBatchNo(param.getBatchNo())){
            throw new ValidationFailedException("批次号已存在");
        }
        // 判断配置和模板是否存在
        if (Objects.nonNull(param.getConfigId())){
            if (!cashierCodeConfigManager.existedById(param.getConfigId())){
                throw new ValidationFailedException("码牌配置不存在");
            }
        }
        if (Objects.nonNull(param.getTemplateId())){
            if (!cashierCodeTemplateManager.existedById(param.getTemplateId())){
                throw new ValidationFailedException("码牌模板不存在");
            }
        }
        var cashierCodes = IntStream.range(0, param.getCount())
                .mapToObj(i -> {
                    String code = param.getBatchNo() + String.format("%03d", i+1);
                    return new CashierCode()
                            .setCode(code)
                            .setBatchNo(param.getBatchNo())
                            .setAmountType(param.getAmountType())
                            .setAmount(param.getAmount())
                            .setConfigId(param.getConfigId())
                            .setTemplateId(param.getTemplateId())
                            .setEnable(param.getEnable());
                })
                .toList();
        cashierCodeManager.saveAll(cashierCodes);
    }

    /**
     * 创建码牌
     */
    public void create(CashierCodeCreateParam param){
        // 判断配置和模板是否存在
        if (Objects.nonNull(param.getConfigId())){
            if (!cashierCodeConfigManager.existedById(param.getConfigId())){
                throw new ValidationFailedException("码牌配置不存在");
            }
        }
        if (Objects.nonNull(param.getTemplateId())){
            if (!cashierCodeTemplateManager.existedById(param.getTemplateId())){
                throw new ValidationFailedException("码牌模板不存在");
            }
        }
        CashierCode entity = CashierCodeConvert.CONVERT.toEntity(param);
        cashierCodeManager.save(entity);
    }

    /**
     * 更新码牌信息
     */
    public void update(CashierCodeUpdateParam param){
        var cashierCode = cashierCodeManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("码牌不存在"));
        // 判断配置和模板是否存在
        if (Objects.nonNull(param.getConfigId())){
            if (!cashierCodeConfigManager.existedById(param.getConfigId())){
                throw new ValidationFailedException("码牌配置不存在");
            }
        }
        if (Objects.nonNull(param.getTemplateId())){
            if (!cashierCodeTemplateManager.existedById(param.getTemplateId())){
                throw new ValidationFailedException("码牌模板不存在");
            }
        }
        // 如果是任意金额类型, 去除金额值
        if (Objects.equals(CashierAmountTypeEnum.RANDOM.getCode(), param.getAmountType())){
            param.setAmount(null);
        }
        BeanUtil.copyProperties(param, cashierCode);
        cashierCodeManager.updateById(cashierCode);
    }


    /**
     * 绑定商户和应用
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindMerchant(AssignMerchantParam param){
        if (StrUtil.isNotBlank(param.getMchNo())){
            var merchant = merchantManager.findByMchNo(param.getMchNo()).orElseThrow(() -> new ValidationFailedException("商户不存在"));
            if (StrUtil.isNotBlank(param.getAppId())){
                var app = mchAppManager.findByAppId(param.getAppId()).orElseThrow(() -> new ValidationFailedException("应用不存在"));
                if (!Objects.equals(merchant.getMchNo(), app.getMchNo())){
                    throw new ValidationFailedException("商户和应用不匹配");
                }
            }
        } else {
            param.setAppId(null);
        }
        cashierCodeManager.lambdaUpdate()
                .set(CashierCode::getMchNo, param.getMchNo())
                .set(CashierCode::getAppId, param.getAppId())
                .in(CashierCode::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }


    /**
     * 商户解绑
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindMerchant(AssignMerchantParam param){
        cashierCodeManager.lambdaUpdate()
                .set(CashierCode::getMchNo, null)
                .set(CashierCode::getAppId, null)
                .in(CashierCode::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();

    }

    /**
     * 绑定应用
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(AssignMerchantParam param){
        if (StrUtil.isNotBlank(param.getAppId())){
            var app = mchAppManager.findByAppId(param.getAppId()).orElseThrow(() -> new ValidationFailedException("应用不存在"));
            if (!Objects.equals(MchContextLocal.getMchNo(), app.getMchNo())){
                throw new ValidationFailedException("商户和应用不匹配");
            }
        }
        cashierCodeManager.lambdaUpdate()
                .set(CashierCode::getAppId, param.getAppId())
                .in(CashierCode::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 应用解绑
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbindApp(AssignMerchantParam param){
        cashierCodeManager.lambdaUpdate()
                .set(CashierCode::getAppId, null)
                .in(CashierCode::getId, param.getIds())
                .setIncrBy(MpRealDelEntity::getVersion, 1)
                .update();
    }

    /**
     * 获取码牌
     */
    public CashierCode getAndCheckCode(String code) {
        var cashierCode = cashierCodeManager.findByCode(code)
                .orElseThrow(() -> new DataNotExistException("码牌不存在"));
        if (!cashierCode.getEnable()) {
            throw new ValidationFailedException("码牌已禁用");
        }
        //判断是否分配了商户号和应用号
        if (cashierCode.getMchNo() == null || cashierCode.getAppId() == null){
            throw new ValidationFailedException("码牌未分配商户和应用");
        }
        return cashierCode;
    }

    /**
     * 删除码牌
     */
    public void delete(Long id) {
        CashierCode cashierCode = cashierCodeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("码牌不存在"));
        // 判断是否已经绑定了商户和应用
        if (cashierCode.getMchNo() != null || cashierCode.getAppId() != null){
//            throw new ValidationFailedException("码牌已绑定商户和应用，请先解绑");
        }
        cashierCodeManager.deleteById(id);
    }


    /**
     * 获取码牌链接
     */
    public String getCodeLink(String code) {
        PlatformUrlConfig urlConfig = platformConfigService.getUrlConfig();
        return StrUtil.format("{}/cashier/code/{}", urlConfig.getGatewayH5Url(),code);
    }

    /**
     * 判断批次号是否存在
     */
    public boolean existsByBatchNo(String batchNo){
        return cashierCodeManager.existedByBatchNo(batchNo);
    }

}
