package cn.bootx.platform.starter.file.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.starter.file.entity.FilePlatform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/8/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class FilePlatformManager extends BaseManager<FilePlatformMapper, FilePlatform> {
}
