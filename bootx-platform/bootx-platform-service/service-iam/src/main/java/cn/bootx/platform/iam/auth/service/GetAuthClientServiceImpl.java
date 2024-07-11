package cn.bootx.platform.iam.auth.service;

import cn.bootx.platform.iam.dao.client.ClientManager;
import cn.bootx.platform.iam.entity.client.Client;
import cn.bootx.platform.starter.auth.authentication.GetAuthClientService;
import cn.bootx.platform.starter.auth.entity.AuthClient;
import cn.bootx.platform.starter.auth.exception.ApplicationNotFoundException;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 获取认证应用
 *
 * @author xxm
 * @since 2022/6/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetAuthClientServiceImpl implements GetAuthClientService {

    private final ClientManager clientManager;

    @Override
    public AuthClient getAuthApplication(String authClientCode) {
        Client client = clientManager.findByCode(authClientCode).orElseThrow(ApplicationNotFoundException::new);
        AuthClient authClient = new AuthClient();
        BeanUtil.copyProperties(client, authClient);
        authClient.setEnable(true);
        return authClient;
    }
}
