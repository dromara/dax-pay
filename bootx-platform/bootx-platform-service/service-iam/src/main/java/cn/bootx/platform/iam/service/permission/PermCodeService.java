package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.iam.dao.permission.PermCodeManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 权限码管理
 *
 * @author xxm
 * @since 2023/1/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermCodeService {
    private final PermCodeManager permCodeManager;

}
