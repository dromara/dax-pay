package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.UnionPayWay;
import cn.bootx.platform.daxpay.service.core.channel.union.dao.UnionPayConfigManager;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.system.config.service.PayChannelConfigService;
import cn.bootx.platform.daxpay.service.param.channel.union.UnionPayConfigParam;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.union.api.UnionPayConfigStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 云闪付支付配置
 * @author xxm
 * @since 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayConfigService {
    /** 默认云闪付配置的主键ID */
    private final static Long ID = 0L;
    private final UnionPayConfigManager unionPayConfigManager;
    private final PayChannelConfigService payChannelConfigService;

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(UnionPayConfigParam param) {
        UnionPayConfig unionPayConfig = unionPayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("支付宝配置不存在"));
        // 启用或停用
        if (!Objects.equals(param.getEnable(), unionPayConfig.getEnable())){
            payChannelConfigService.setEnable(PayChannelEnum.UNION_PAY.getCode(), param.getEnable());
        }

        BeanUtil.copyProperties(param, unionPayConfig, CopyOptions.create().ignoreNullValue());
        unionPayConfigManager.updateById(unionPayConfig);
    }

    /**
     * 云闪付支持支付方式
     */
    public List<LabelValue> findPayWays() {
        return UnionPayWay.getPayWays()
                .stream()
                .map(e -> new LabelValue(e.getName(),e.getCode()))
                .collect(Collectors.toList());
    }

    /**
     * 获取支付配置
     */
    public UnionPayConfig getConfig(){
        return unionPayConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("云闪付支付配置不存在"));
    }

    /**
     * 获取并检查支付配置
     */
    public UnionPayConfig getAndCheckConfig() {
        UnionPayConfig unionPayConfig = this.getConfig();
        if (!unionPayConfig.getEnable()){
            throw new PayFailureException("云闪付支付未启用");
        }
        return unionPayConfig;
    }


    /**
     * 生成云闪付支付服务
     */
    public UnionPayKit initPayService(UnionPayConfig config){
        UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
        unionPayConfigStorage.setInputCharset(CharsetUtil.UTF_8);
        // 商户号
        unionPayConfigStorage.setMerId(config.getMachId());
        //是否为证书签名
        unionPayConfigStorage.setCertSign(config.isCertSign());

        //中级证书 流
        unionPayConfigStorage.setAcpMiddleCert(new ByteArrayInputStream(Base64.decode(config.getAcpMiddleCert())));
        //根证书 流
        unionPayConfigStorage.setAcpRootCert(new ByteArrayInputStream(Base64.decode(config.getAcpRootCert())));
        // 私钥证书 流
        unionPayConfigStorage.setKeyPrivateCert(new ByteArrayInputStream(Base64.decode(config.getKeyPrivateCert())));

        //私钥证书对应的密码 私钥证书对应的密码
        unionPayConfigStorage.setKeyPrivateCertPwd(config.getKeyPrivateCertPwd());
        //设置证书对应的存储方式，证书字符串信息
        unionPayConfigStorage.setCertStoreType(CertStoreType.INPUT_STREAM);

        // 回调地址
        unionPayConfigStorage.setNotifyUrl(config.getNotifyUrl());
        // 同步回调可不填
        unionPayConfigStorage.setReturnUrl(config.getReturnUrl());
        unionPayConfigStorage.setSignType(config.getSignType());
        //是否为测试账号，沙箱环境
        unionPayConfigStorage.setTest(config.isSandbox());

        // 网络请求配置
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        httpConfigStorage.setCertStoreType(CertStoreType.INPUT_STREAM);
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);

        // 创建支付服务
        return new UnionPayKit(unionPayConfigStorage, httpConfigStorage);
    }

}
