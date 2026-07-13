package cn.daxpay.open.platform.iam.service.client;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.result.client.ClientResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/// # 登录终端主数据查询
///
/// 固定三端身份域, 数据源为 [ClientEnum] + i18n, 不落库、无启停.
///
@Service
@RequiredArgsConstructor
public class ClientQueryService {

    /// 全部终端(admin / merchant / gateway)
    public List<ClientResult> findAll() {
        return Arrays.stream(ClientEnum.values())
                .map(this::toResult)
                .toList();
    }

    private ClientResult toResult(ClientEnum client) {
        return new ClientResult()
                .setCode(client.getCode())
                .setName(I18nUtil.getEnumName(client));
    }
}
