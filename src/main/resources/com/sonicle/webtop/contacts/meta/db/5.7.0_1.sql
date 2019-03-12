@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Remove obsolete setting keys
-- ----------------------------
@DataSource[default@com.sonicle.webtop.core]
DELETE FROM "core"."user_settings" WHERE ("user_settings"."service_id" = 'com.sonicle.webtop.contacts') AND ("user_settings"."key" = 'category.roots.checked');
DELETE FROM "core"."user_settings" WHERE ("user_settings"."service_id" = 'com.sonicle.webtop.contacts') AND ("user_settings"."key" = 'category.folders.checked');
