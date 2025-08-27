package cn.bootx.platform.iam.service.client;

import cn.bootx.platform.common.headerholder.local.HolderContextHolder;
import cn.bootx.platform.core.code.CommonCode;

/**
 * 获取终端编码
 * @author xxm
 * @since 2025/1/31
 */
public interface ClientCodeService {

    /**
     * 获取终端编码
     */
    default String getClientCode(){
        // 从请求头获取当前终端类型
        return HolderContextHolder.get(CommonCode.CLIENT);
    }
}
