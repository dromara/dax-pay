package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.enums.WechatTransferSceneEnum;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectChannelMerchantUpdateParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectChannelMerchantResult;
import cn.daxpay.open.channel.wechat.result.direct.WechatTransferSceneOptionResult;
import cn.daxpay.open.channel.wechat.strategy.direct.merchant.WechatDirectChannelMerchantCleanupStrategy;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/// # 微信直连通道商户管理
///
/// 一个微信商户号(wxMchId)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
/// 通道商户删除时的扩展数据清理由独立的策略类
/// [WechatDirectChannelMerchantCleanupStrategy] 承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;

    /// 创建微信直连通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(WechatDirectChannelMerchantCreateParam param) {
        // 校验同一商户下微信商户号不重复
        if (wechatDirectChannelMerchantManager.existsByMchNoAndWxMchId(
                param.getMchNo(), param.getWxMchId())) {
            // 微信: 同一商户下该微信商户已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.directMchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("WECHAT");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        // 沙箱标记从支付产品生效环境同步写入, 禁止商户/表单设置
        boolean sandbox = payProductConfigManager.isSandboxActive(param.getProduct());
        channelMerchant.setSandbox(sandbox);
        channelMerchantManager.save(channelMerchant);
        // 写直连绑定表(wxMchId 作为业务字段, 不参与关联)
        var entity = new WechatDirectChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setWxMchId(param.getWxMchId());
        entity.setTransferScene(param.getTransferScene());
        wechatDirectChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询直连通道商户配置
    public WechatDirectChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return wechatDirectChannelMerchantManager.lambdaQuery()
                .eq(WechatDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(WechatDirectChannelMerchant::toResult)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 更新微信直连通道商户(转账场景/微信商户号)
    @Transactional(rollbackFor = Exception.class)
    public void update(WechatDirectChannelMerchantUpdateParam param) {
        WechatDirectChannelMerchant entity = wechatDirectChannelMerchantManager.lambdaQuery()
                .eq(WechatDirectChannelMerchant::getChannelMchNo, param.getChannelMchNo())
                .oneOpt()
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 微信商户号变更时校验同一商户下不重复
        if (param.getWxMchId() != null && !param.getWxMchId().equals(entity.getWxMchId())) {
            if (wechatDirectChannelMerchantManager.existsByMchNoAndWxMchId(
                    entity.getMchNo(), param.getWxMchId())) {
                // 微信: 同一商户下该微信商户已存在
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.directMchDuplicate");
            }
            entity.setWxMchId(param.getWxMchId());
        }
        // 转账场景(允许清空, 清空后发起转账会报"场景未配置")
        if (param.getTransferScene() != null) {
            entity.setTransferScene(param.getTransferScene());
        }
        wechatDirectChannelMerchantManager.updateById(entity);
    }

    /// 查询微信转账场景选项列表(供前端下拉与报备字段动态渲染)
    public List<WechatTransferSceneOptionResult> findSceneOptions() {
        return Arrays.stream(WechatTransferSceneEnum.values())
                .map(scene -> new WechatTransferSceneOptionResult()
                        .setCode(scene.getCode())
                        .setName(scene.getName())
                        .setReportInfoTypes(scene.getReportInfoTypes())
                        .setReportInfoDescriptions(scene.getReportInfoDescriptions())
                        .setUserRecvPerceptionOptions(scene.getUserRecvPerceptionOptions()))
                .toList();
    }
}
