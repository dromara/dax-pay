package cn.bootx.platform.baseapi.code;

/**
 * 错误码定义类 错误码范围：21000-21999
 *
 * @author xxm
 */
public interface BspErrorCodes {

    /**
     * MD5密码错误
     */
    int MD5_ENCODE_ERROR = 21001;

    // 注册相关错误
    /** 租户已存在 */
    int TENANT_ALREADY_EXISTED = 21100;

    /** 租户不存在 */
    int TENANT_NOT_EXISTED = 21101;

    /** 应用已存在 */
    int APP_ALREADY_EXISTED = 21200;

    /** 应用不存在 */
    int APP_NOT_EXISTED = 21201;

    /** 租户不存在或未激活 */
    int TENANT_NOT_EXISTED_OR_NOT_ACTIVE = 21202;

    // 权限相关错误
    /** 用户已存在 */
    int USER_ALREADY_EXISTED = 21300;

    /** 用户不存在 */
    int USER_NOT_EXISTED = 21301;

    /** 错误的用户名或密码 */
    int WRONG_USERNAME_OR_PASSWORD = 21302;

    /** 用户密码输入错误 */
    int USER_WRONG_PASSWORD = 21303;

    /** 角色已存在 */
    int ROLE_ALREADY_EXISTED = 21310;

    /** 角色不存在 */
    int ROLE_NOT_EXISTED = 21311;

    /** 角色已经被使用 */
    int ROLE_ALREADY_USED = 21312;

    /** 资源已存在 */
    int RESOURCE_ALREADY_EXISTED = 21320;

    /** 资源不存在 */
    int RESOURCE_NOT_EXISTED = 21321;

    /** 资源已经被使用 */
    int RESOURCE_ALREADY_USED = 21322;

    /** 缓存错误 */
    int CACHE_ERROR = 21330;

    /** 权限操作错误 */
    int PERMISSION_DB_ERROR = 21331;

    /** 没有访问权限 */
    int PERMISSION_NOT_EXIST = 21340;

    /** 用户 id 不可为空 */
    int USER_ID_CANNOT_BE_NULL_ERROR = 21341;

    /** 生成图片字节数组异常 */
    int CODE_TO_BYTEARRAY_ERROR = 21433;

    /** 没有加载权限 */
    int PERMISSION_NOT_LOAD = 21434;

    /** code already exists */
    int CODE_EXISTS = 21435;

    /** 文件上传失败 */
    int FILE_UPLOAD_FAIL = 21436;

    /** 本地字体初始化异常 */
    int LOCAL_FONT_INIT_ERROR = 21437;

    /** 维护主题名称已存在 */
    int MAINTAIN_TOPIC_ALREADY_EXISTED = 21438;

    /** appId不唯一 */
    int APP_ID_UNIQUENESS_CHECK_FAIL = 21439;

    /** appName不唯一 */
    int APP_NAME_UNIQUENESS_CHECK_FAIL = 21440;

    /** 上传app版本时app分发索引页还没有上传 */
    int LANDING_PAGE_NOT_EXIST = 21441;

    /** appversion参数client错误 */
    int APP_VERSION_CLIENT_VALIDATION_ERROR = 21442;

    /** 维护主题未找到 */
    int MAINTAIN_TOPIC_NOTFOUND = 21443;

    // 基础数据相关错误
    /** 字典已存在 */
    int DICTIONARY_ALREADY_EXISTED = 21400;

    /** 字典不存在 */
    int DICTIONARY_NOT_EXISTED = 21401;

    /** 字典项已存在 */
    int DICTIONARY_ITEM_ALREADY_EXISTED = 21410;

    /** 字典项不存在 */
    int DICTIONARY_ITEM_NOT_EXISTED = 21411;

    /** 字典项已使用 */
    int DICTIONARY_ITEM_ALREADY_USED = 21412;

    /** 城市已存在 */
    int CITY_ALREADY_EXISTED = 21420;

    /** 城市不存在 */
    int CITY_NOT_EXISTED = 21421;

    /** 用户TOKEN无效 */
    int USER_TOKEN_INVALID = 21430;

    /** 用户禁止使用 */
    int USER_FORBIDDEN = 21431;

    /** 子项目存在，不可删除 */
    int CHILD_ITEM_EXISTED = 21432;

    /**
     * 渠道已存在
     */
    int CHANNEL_ALREADY_EXISTS = 21444;

}
