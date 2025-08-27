package cn.bootx.platform.starter.file.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.starter.file.entity.FilePlatform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付平台配置
 * @author xxm
 * @since 2024/8/12
 */
@Slf4j
@Deprecated
@Repository
@RequiredArgsConstructor
public class FilePlatformManager extends BaseManager<FilePlatformMapper, FilePlatform> {

    /**
     * 根据平台类型查询
     */
    public Optional<FilePlatform> findByType(String platform) {
        return findByField(FilePlatform::getType, platform);
    }
}
