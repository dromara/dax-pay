package cn.bootx.platform.daxpay.core.openapi.service;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.common.context.ApiInfoLocal;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.openapi.dao.PayOpenApiManager;
import cn.bootx.platform.daxpay.core.openapi.entity.PayOpenApi;
import cn.bootx.platform.daxpay.param.openapi.PayOpenApiInfoParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 开放接口信息
 * @author xxm
 * @since 2023/12/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOpenApiService {
    private final PayOpenApiManager openApiInfoManager;

    /**
     * 编辑
     */
    public void update(PayOpenApiInfoParam param){

    }

    /**
     * 分页
     */
    public void page(PageParam pageParam){

    }

    /**
     * 根据ID获取
     */
    public void findById(Long id){

    }

    /**
     * 初始化接口上下文信息
     */
    public void initApiInfo(PayOpenApi api){
        // 记录支付接口信息
        ApiInfoLocal apiInfoLocal = PaymentContextLocal.get().getApiInfo();
        apiInfoLocal.setApiCode(api.getCode())
                .setReqSign(api.isReqSign())
                .setResSign(api.isResSign())
                .setNotice(api.isNotice())
                .setNoticeSign(api.isNoticeSign())
                .setRecord(api.isRecord());
    }


}
