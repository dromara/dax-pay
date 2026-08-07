package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.convert.direct.WechatTransferConfigConvert;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatTransferConfigManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.entity.direct.WechatTransferConfig;
import cn.daxpay.open.channel.wechat.enums.WechatTransferSceneEnum;
import cn.daxpay.open.channel.wechat.param.direct.WechatTransferConfigParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatTransferConfigResult;
import cn.daxpay.open.payment.wx.dao.merchant.WxMchAppManager;
import cn.daxpay.open.payment.wx.entity.merchant.WxMchApp;
import cn.daxpay.open.payment.wx.enums.WxAppTypeEnum;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/// # 微信转账配置
///
/// 管理通道商户的转账配置(一对一: 转账场景 + 转账发起应用)。
/// 发起转账时由转账策略读取本配置注入场景并按 [WechatTransferConfig#getTransferAppRefId]
/// 解析发起应用(公众号)的 wxAppId, 替代通道商户表上的单值 transferScene。
///
/// 运营端写 [WechatTransferConfig](MchBaseEntity) 显式 setMchNo, 避免上下文缺失。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatTransferConfigService {

    private final WechatTransferConfigManager wechatTransferConfigManager;
    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;
    private final WxMchAppManager wxMchAppManager;

    /// 查询通道商户的转账配置(一对一, 未配置返回 null)
    ///
    /// @param mchNo        商户号(归属校验)
    /// @param channelMchNo 通道商户号
    /// @return 转账配置(含冗余展示), 不存在返回 null
    public WechatTransferConfigResult findByChannelMchNo(String mchNo, String channelMchNo) {
        assertChannelMerchant(mchNo, channelMchNo);
        return wechatTransferConfigManager.findByChannelMchNo(channelMchNo)
                .map(this::toResultWithMeta)
                .orElse(null);
    }

    /// 保存或更新转账配置(一对一 upsert)
    ///
    /// transferScene / transferAppRefId 均允许为空(支持分步配置或清空),
    /// 但发起转账时两者必须齐备, 由转账策略校验。
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(WechatTransferConfigParam param) {
        // 校验通道商户存在与归属
        assertChannelMerchant(param.getMchNo(), param.getChannelMchNo());
        // 校验发起应用(若指定): 存在 + 归属 + 公众号类型
        if (param.getTransferAppRefId() != null) {
            WxMchApp app = wxMchAppManager.lambdaQuery()
                    .eq(WxMchApp::getId, param.getTransferAppRefId())
                    .oneOpt()
                    .orElseThrow(() -> new DataNotExistException("error.channel.wechat.transferAppNotExist"));
            if (!Objects.equals(app.getMchNo(), param.getMchNo())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.transferAppNotBelong");
            }
            if (!Objects.equals(app.getAppType(), WxAppTypeEnum.OFFICIAL_ACCOUNT.getCode())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.wechat.transferAppTypeNotOfficialAccount");
            }
        }
        // upsert: 存在则全量覆盖(含清空), 不存在则新增
        Optional<WechatTransferConfig> existing = wechatTransferConfigManager
                .findByChannelMchNo(param.getChannelMchNo());
        if (existing.isPresent()) {
            WechatTransferConfig entity = existing.get();
            entity.setTransferScene(param.getTransferScene());
            entity.setTransferAppRefId(param.getTransferAppRefId());
            wechatTransferConfigManager.updateById(entity);
        } else {
            WechatTransferConfig entity = WechatTransferConfigConvert.CONVERT.toEntity(param);
            // 运营端写 MchBaseEntity 必须显式 setMchNo(父类 setter 返回类型不匹配, 单独赋值)
            entity.setMchNo(param.getMchNo());
            wechatTransferConfigManager.save(entity);
        }
    }

    /// 删除通道商户的转账配置(通道商户删除时级联清理)
    public void deleteByChannelMchNo(String channelMchNo) {
        wechatTransferConfigManager.deleteByChannelMchNo(channelMchNo);
    }

    /// 校验通道商户存在且归属匹配
    private void assertChannelMerchant(String mchNo, String channelMchNo) {
        WechatDirectChannelMerchant channelMerchant = wechatDirectChannelMerchantManager
                .findByChannelMchNo(channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        if (!Objects.equals(channelMerchant.getMchNo(), mchNo)) {
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.payment.wx.channelMerchantMismatch");
        }
    }

    /// 转Result并填充冗余展示(场景名 + 发起应用信息)
    private WechatTransferConfigResult toResultWithMeta(WechatTransferConfig entity) {
        WechatTransferConfigResult result = entity.toResult();
        // 场景名(枚举推导)
        if (StrUtil.isNotBlank(entity.getTransferScene())) {
            WechatTransferSceneEnum scene = WechatTransferSceneEnum.findByCode(entity.getTransferScene());
            if (scene != null) {
                result.setSceneName(scene.getName());
            }
        }
        // 发起应用展示信息
        if (entity.getTransferAppRefId() != null) {
            wxMchAppManager.lambdaQuery()
                    .eq(WxMchApp::getId, entity.getTransferAppRefId())
                    .oneOpt()
                    .ifPresent(app -> {
                        result.setTransferAppName(app.getAppName());
                        result.setWxAppId(app.getWxAppId());
                        result.setAppType(app.getAppType());
                    });
        }
        return result;
    }
}
