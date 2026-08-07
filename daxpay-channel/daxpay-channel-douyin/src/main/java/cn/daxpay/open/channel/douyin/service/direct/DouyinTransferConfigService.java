package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.convert.direct.DouyinTransferConfigConvert;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinTransferConfigManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinTransferConfig;
import cn.daxpay.open.channel.douyin.param.direct.DouyinTransferConfigParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinTransferConfigResult;
import cn.daxpay.open.payment.douyin.dao.merchant.DyMchAppManager;
import cn.daxpay.open.payment.douyin.entity.merchant.DyMchApp;
import cn.daxpay.open.payment.douyin.enums.DyAppTypeEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/// # 抖音转账配置
///
/// 管理通道商户的转账配置(一对一: 转账发起应用)。
/// 发起转账时由转账策略读取本配置按 [DouyinTransferConfig#getTransferAppRefId]
/// 解析发起应用(网站应用)的 douyinAppId, 决定转出主体与收款人 openId 的来源。
///
/// 运营端写 [DouyinTransferConfig](MchBaseEntity) 显式 setMchNo, 避免上下文缺失。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinTransferConfigService {

    private final DouyinTransferConfigManager douyinTransferConfigManager;
    private final DouyinDirectChannelMerchantManager douyinDirectChannelMerchantManager;
    private final DyMchAppManager dyMchAppManager;

    /// 查询通道商户的转账配置(一对一, 未配置返回 null)
    ///
    /// @param mchNo        商户号(归属校验)
    /// @param channelMchNo 通道商户号
    /// @return 转账配置(含冗余展示), 不存在返回 null
    public DouyinTransferConfigResult findByChannelMchNo(String mchNo, String channelMchNo) {
        assertChannelMerchant(mchNo, channelMchNo);
        return douyinTransferConfigManager.findByChannelMchNo(channelMchNo)
                .map(this::toResultWithMeta)
                .orElse(null);
    }

    /// 保存或更新转账配置(一对一 upsert)
    ///
    /// transferAppRefId 允许为空(支持清空), 但发起转账时必须已配置, 由转账策略校验。
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(DouyinTransferConfigParam param) {
        // 校验通道商户存在与归属
        assertChannelMerchant(param.getMchNo(), param.getChannelMchNo());
        // 校验发起应用(若指定): 存在 + 归属 + 网站应用类型(仅网站应用支持手机H5获取OpenId)
        if (param.getTransferAppRefId() != null) {
            DyMchApp app = dyMchAppManager.findById(param.getTransferAppRefId())
                    .orElseThrow(() -> new DataNotExistException("error.channel.douyin.transferAppNotExist"));
            if (!Objects.equals(app.getMchNo(), param.getMchNo())) {
                // 抖音: 转账发起应用不属于当前商户
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.transferAppNotBelong");
            }
            if (!Objects.equals(app.getAppType(), DyAppTypeEnum.WEB_APP.getCode())) {
                // 抖音: 转账发起应用必须是网站应用类型
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.douyin.transferAppTypeNotWebApp");
            }
        }
        // upsert: 存在则全量覆盖(含清空), 不存在则新增
        Optional<DouyinTransferConfig> existing = douyinTransferConfigManager
                .findByChannelMchNo(param.getChannelMchNo());
        if (existing.isPresent()) {
            DouyinTransferConfig entity = existing.get();
            entity.setTransferAppRefId(param.getTransferAppRefId());
            douyinTransferConfigManager.updateById(entity);
        } else {
            DouyinTransferConfig entity = DouyinTransferConfigConvert.CONVERT.toEntity(param);
            // 运营端写 MchBaseEntity 必须显式 setMchNo(父类 setter 返回类型不匹配, 单独赋值)
            entity.setMchNo(param.getMchNo());
            douyinTransferConfigManager.save(entity);
        }
    }

    /// 删除通道商户的转账配置(通道商户删除时级联清理)
    public void deleteByChannelMchNo(String channelMchNo) {
        douyinTransferConfigManager.deleteByChannelMchNo(channelMchNo);
    }

    /// 校验通道商户存在且归属匹配
    private void assertChannelMerchant(String mchNo, String channelMchNo) {
        DouyinDirectChannelMerchant channelMerchant = douyinDirectChannelMerchantManager
                .findByChannelMchNo(channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        if (!Objects.equals(channelMerchant.getMchNo(), mchNo)) {
            // 抖音: 通道商户与商户号不匹配
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.douyin.channelMerchantMismatch");
        }
    }

    /// 转Result并填充冗余展示(发起应用信息)
    private DouyinTransferConfigResult toResultWithMeta(DouyinTransferConfig entity) {
        DouyinTransferConfigResult result = entity.toResult();
        // 发起应用展示信息
        if (entity.getTransferAppRefId() != null) {
            dyMchAppManager.findById(entity.getTransferAppRefId())
                    .ifPresent(app -> {
                        result.setTransferAppName(app.getAppName());
                        result.setDouyinAppId(app.getDouyinAppId());
                        result.setAppType(app.getAppType());
                    });
        }
        return result;
    }
}
