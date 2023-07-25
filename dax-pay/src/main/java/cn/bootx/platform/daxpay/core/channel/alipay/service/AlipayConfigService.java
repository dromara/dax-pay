package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.code.MchAndAppCode;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.paymodel.AliPayCode;
import cn.bootx.platform.daxpay.code.paymodel.AliPayWay;
import cn.bootx.platform.daxpay.core.channel.alipay.dao.AlipayConfigManager;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import cn.bootx.platform.daxpay.core.merchant.service.MchAppPayConfigService;
import cn.bootx.platform.daxpay.core.merchant.service.MchAppService;
import cn.bootx.platform.daxpay.dto.channel.alipay.AlipayConfigDto;
import cn.bootx.platform.daxpay.param.channel.alipay.AlipayConfigParam;
import cn.bootx.platform.daxpay.param.channel.alipay.AlipayConfigQuery;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付宝支付
 *
 * @author xxm
 * @since 2020/12/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayConfigService {

    private final AlipayConfigManager alipayConfigManager;

    private final MchAppService mchAppService;

    private final MchAppPayConfigService appPayConfigService;

    /**
     * 添加支付宝配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(AlipayConfigParam param) {
        // 是否有管理关系判断
        if (!mchAppService.checkMatch(param.getMchCode(), param.getMchAppCode())) {
            throw new BizException("应用信息与商户信息不匹配");
        }
        AlipayConfig alipayConfig = AlipayConfig.init(param);
        alipayConfig.setState(MchAndAppCode.PAY_CONFIG_STATE_NORMAL);
        alipayConfigManager.save(alipayConfig);

        // 保存关联关系
        MchAppPayConfig mchAppPayConfig = new MchAppPayConfig().setAppCode(alipayConfig.getMchAppCode())
            .setConfigId(alipayConfig.getId())
            .setChannel(PayChannelEnum.ALI.getCode())
            .setState(alipayConfig.getState());
        appPayConfigService.add(mchAppPayConfig);
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(AlipayConfigParam param) {
        AlipayConfig alipayConfig = alipayConfigManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param, alipayConfig, CopyOptions.create().ignoreNullValue());
        // 支付方式
        if (CollUtil.isNotEmpty(param.getPayWayList())) {
            alipayConfig.setPayWays(String.join(",", param.getPayWayList()));
        }
        else {
            alipayConfig.setPayWays(null);
        }
        alipayConfigManager.updateById(alipayConfig);
    }

    /**
     * 获取
     */
    public AlipayConfigDto findById(Long id) {
        return alipayConfigManager.findById(id).map(AlipayConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    public PageResult<AlipayConfigDto> page(PageParam pageParam, AlipayConfigQuery param) {
        return MpUtil.convert2DtoPageResult(alipayConfigManager.page(pageParam, param));
    }

    /**
     * 支付宝支持支付方式
     */
    public List<LabelValue> findPayWayList() {
        return AliPayWay.getPayWays()
            .stream()
            .map(e -> new LabelValue(e.getName(),e.getCode()))
            .collect(Collectors.toList());
    }

    /**
     * 初始化IJPay服务,通过商户应用AppCode获取支付配置
     */
    public void initApiConfigByMchAppCode(String mchAppCode){
         val alipayConfig = alipayConfigManager.findByMchAppCode(mchAppCode)
                .orElseThrow(() -> new DataNotExistException("支付配置不存在"));
         this.initApiConfig(alipayConfig);
    }

    /**
     * 初始化IJPay服务
     */
    @SneakyThrows
    public void initApiConfig(AlipayConfig alipayConfig) {

        AliPayApiConfig aliPayApiConfig;
        // 公钥
        if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_KEY)) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(alipayConfig.getAppId())
                    .setPrivateKey(alipayConfig.getPrivateKey())
                    .setAliPayPublicKey(alipayConfig.getAlipayPublicKey())
                    .setCharset(CharsetUtil.UTF_8)
                    .setServiceUrl(alipayConfig.getServerUrl())
                    .setSignType(alipayConfig.getSignType())
                    .build();
        }
        // 证书
        else if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_CART)) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(alipayConfig.getAppId())
                    .setPrivateKey(alipayConfig.getPrivateKey())
                    .setAppCertContent(alipayConfig.getAppCert())
                    .setAliPayCertContent(alipayConfig.getAlipayCert())
                    .setAliPayRootCertContent(alipayConfig.getAlipayRootCert())
                    .setCharset(CharsetUtil.UTF_8)
                    .setServiceUrl(alipayConfig.getServerUrl())
                    .setSignType(alipayConfig.getSignType())
                    .buildByCertContent();
        }
        else {
            throw new BizException("支付宝认证方式不可为空");
        }
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }

}
