-- 用户工作台快捷入口偏好表: 用户个性化工作台快捷入口(显隐+排序), PC与移动按 client_code 分开管理

CREATE TABLE IF NOT EXISTS "public"."iam_user_dashboard_preference" (
    "id"                  bigint       NOT NULL PRIMARY KEY,
    "user_id"             bigint       NOT NULL,
    "client_code"         varchar(32)  NOT NULL,
    "entries"             jsonb        NOT NULL DEFAULT '[]'::jsonb,
    "creator"             bigint,
    "create_time"         timestamptz(6),
    "last_modifier"       bigint,
    "last_modified_time"  timestamptz(6),
    "version"             integer      NOT NULL DEFAULT 0,
    "deleted"             boolean      NOT NULL DEFAULT false
);

COMMENT ON TABLE  "public"."iam_user_dashboard_preference" IS '用户工作台快捷入口偏好';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."client_code" IS '终端编码(WEB/MOBILE), PC与移动分开管理';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."entries" IS '已选快捷入口有序序列(纯key数组), 如 ["merchant","notify"]';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."deleted" IS '逻辑删除标志';

-- 每用户每终端唯一一份配置(仅在未逻辑删除时生效, 允许删后重建)
CREATE UNIQUE INDEX IF NOT EXISTS "uk_user_das_pref_user_client"
    ON "public"."iam_user_dashboard_preference" ("user_id", "client_code")
    WHERE "deleted" = false;
