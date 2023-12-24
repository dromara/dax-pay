package cn.bootx.platform.daxpay.core.openapi.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.core.openapi.entity.PayOpenApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付开放接口管理
 * @author xxm
 * @since 2023/12/22
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOpenApiManager extends BaseManager<PayOpenApiMapper, PayOpenApi> {

    /**
     * 根据code查询
     */
    public Optional<PayOpenApi> findByCode(String code){
        return findByField(PayOpenApi::getCode,code);
    }

    /**
     * 根据api查询
     */
    public Optional<PayOpenApi> findByApi(String api){
        return findByField(PayOpenApi::getApi,api);
    }

}
