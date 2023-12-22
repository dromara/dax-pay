package cn.bootx.platform.daxpay.core.openapi.service;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.openapi.dao.PayOpenApiInfoManager;
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
public class PayOpenApiInfoService {
    private final PayOpenApiInfoManager openApiInfoManager;

    /**
     * 编辑
     */
    public void update(){

    }

    /**
     * 分页
     */
    public void page(PageParam pageParam){

    }

    /**
     *
     */
    public void findById(Long id){

    }


}
