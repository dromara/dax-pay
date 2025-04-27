package org.dromara.daxpay.server.service;

import cn.bootx.platform.iam.service.client.ClientCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.springframework.stereotype.Service;

/**
 * 终端获取类
 * @author xxm
 * @since 2025/1/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientCodeServiceImpl implements ClientCodeService {

    @Override
    public String getClientCode() {
        return DaxPayCode.Client.ADMIN;
    }
}
