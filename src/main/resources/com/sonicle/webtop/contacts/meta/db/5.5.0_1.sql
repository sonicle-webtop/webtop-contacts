@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Fix view setting values
-- ----------------------------
@DataSource[default@com.sonicle.webtop.core]
UPDATE "core"."user_settings" SET "value" = 'work' WHERE ("user_settings"."service_id" = 'com.sonicle.webtop.contacts') AND ("user_settings"."key" = 'view') AND ("user_settings"."value" = 'w');
UPDATE "core"."user_settings" SET "value" = 'home' WHERE ("user_settings"."service_id" = 'com.sonicle.webtop.contacts') AND ("user_settings"."key" = 'view') AND ("user_settings"."value" = 'h');
