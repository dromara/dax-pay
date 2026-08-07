package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayTransferConfigConvert;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectChannelMerchantManager;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayTransferConfigManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferConfig;
import cn.daxpay.open.channel.alipay.param.direct.AlipayTransferConfigParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayTransferConfigResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/// # 支付宝转账配置
///
/// 管理通道商户的转账配置(一对一: 转账转出应用)。
/// 发起转账时由转账策略读取本配置按 [AlipayTransferConfig#getTransferAppRefId]
/// 解析转出应用(支付宝直连应用)的 aliAppId 与密钥。
///
/// 运营端写 [AlipayTransferConfig](MchBaseEntity) 显式 setMchNo, 避免上下文缺失。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayTransferConfigService {

    private final AlipayTransferConfigManager alipayTransferConfigManager;
    private final AlipayDirectChannelMerchantManager alipayDirectChannelMerchantManager;
    private final AlipayDirectAppManager alipayDirectAppManager;

    /// 查询通道商户的转账配置(一对一, 未配置返回 null)
    ///
    /// @param mchNo        商户号(归属校验)
    /// @param channelMchNo 通道商户号
    /// @return 转账配置(含冗余展示), 不存在返回 null
    public AlipayTransferConfigResult findByChannelMchNo(String mchNo, String channelMchNo) {
        assertChannelMerchant(mchNo, channelMchNo);
        return alipayTransferConfigManager.findByChannelMchNo(channelMchNo)
                .map(this::toResultWithMeta)
                .orElse(null);
    }

    /// 保存或更新转账配置(一对一 upsert, 转账应用必填)
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(AlipayTransferConfigParam param) {
        // 校验通道商户存在与归属
        assertChannelMerchant(param.getMchNo(), param.getChannelMchNo());
        // 校验转出应用: 存在 + 归属
        AlipayDirectApp app = alipayDirectAppManager.lambdaQuery()
                .eq(AlipayDirectApp::getId, param.getTransferAppRefId())
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.transferAppNotExist"));
        if (!Objects.equals(app.getMchNo(), param.getMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.alipay.transferAppNotBelong");
        }
        // upsert: 存在则全量覆盖, 不存在则新增
        Optional<AlipayTransferConfig> existing = alipayTransferConfigManager
                .findByChannelMchNo(param.getChannelMchNo());
        if (existing.isPresent()) {
            AlipayTransferConfig entity = existing.get();
            entity.setTransferAppRefId(param.getTransferAppRefId());
            alipayTransferConfigManager.updateById(entity);
        } else {
            AlipayTransferConfig entity = AlipayTransferConfigConvert.CONVERT.toEntity(param);
            // 运营端写 MchBaseEntity 必须显式 setMchNo(父类 setter 返回类型不匹配, 单独赋值)
            entity.setMchNo(param.getMchNo());
            alipayTransferConfigManager.save(entity);
        }
    }

    /// 删除通道商户的转账配置(通道商户删除时级联清理)
    public void deleteByChannelMchNo(String channelMchNo) {
        alipayTransferConfigManager.deleteByChannelMchNo(channelMchNo);
    }

    /// 校验通道商户存在且归属匹配
    private void assertChannelMerchant(String mchNo, String channelMchNo) {
        AlipayDirectChannelMerchant channelMerchant = alipayDirectChannelMerchantManager.lambdaQuery()
                .eq(AlipayDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        if (!Objects.equals(channelMerchant.getMchNo(), mchNo)) {
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.alipay.channelMerchantMismatch");
        }
    }

    /// 转Result并填充冗余展示(转出应用信息)
    private AlipayTransferConfigResult toResultWithMeta(AlipayTransferConfig entity) {
        AlipayTransferConfigResult result = entity.toResult();
        // 转出应用展示信息
        if (entity.getTransferAppRefId() != null) {
            alipayDirectAppManager.lambdaQuery()
                    .eq(AlipayDirectApp::getId, entity.getTransferAppRefId())
                    .oneOpt()
                    .ifPresent(app -> {
                        result.setTransferAppName(app.getAppName());
                        result.setAliAppId(app.getAliAppId());
                        result.setAppType(app.getAppType());
                    });
        }
        return result;
    }
}
