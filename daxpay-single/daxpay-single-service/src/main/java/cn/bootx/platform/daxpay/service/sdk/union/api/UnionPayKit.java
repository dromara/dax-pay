package cn.bootx.platform.daxpay.service.sdk.union.api;

import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.union.api.UnionPayConfigStorage;
import com.egzosn.pay.union.api.UnionPayService;

/**
 * 云闪付支付服务类重命名, 避免与系统中类名冲突
 * @author xxm
 * @since 2024/3/8
 */
public class UnionPayKit extends UnionPayService {
    /**
     * 构造函数
     *
     * @param payConfigStorage 支付配置
     */
    public UnionPayKit(UnionPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public UnionPayKit(UnionPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }
}
