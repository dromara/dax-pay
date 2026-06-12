package org.dromara.daxpay.payment.common.service.assist;

import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.entity.UserDetail;
import org.dromara.daxpay.platform.core.code.DaxPayCode;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # H5端进件时Token管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class OnbH5ApplyAssistService {
    private final Cache<String, String> cache = new TimedCache<>(1000 * 60 * 60 * 24L);

    /// 获取网关端虚拟用户token
    public String token(){
        String token = cache.get("tokenValue");
        if (StrUtil.isNotBlank(token)){
            return token;
        }
        var saLoginModel = new SaLoginParameter()
                .setDeviceType(ClientEnum.GATEWAY.getCode());

        StpUtil.login(DaxPayCode.GATEWAY_USER_ID, saLoginModel);
        SaSession session = StpUtil.getSession();
        UserDetail userDetail = new UserDetail()
                .setId(DaxPayCode.GATEWAY_USER_ID)
                .setAccount(DaxPayCode.GATEWAY_USER_ID.toString())
                .setClientCode(ClientEnum.GATEWAY.getCode())
                .setName("网关支付虚拟用户");
        session.set(CommonCode.USER, userDetail);
        String tokenValue = StpUtil.getTokenValue();
        cache.put("tokenValue", tokenValue);
        return tokenValue;
    }

}

