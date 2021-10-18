@DataSource[default@com.sonicle.webtop.core]

-- ----------------------------
-- Deprecate is_default: move values to user_settings
-- ----------------------------
INSERT INTO "core"."user_settings" ("domain_id", "user_id", "service_id", "key", "value")
(
SELECT DISTINCT ON ("categories"."domain_id", "categories"."user_id") "categories"."domain_id", "categories"."user_id", 'com.sonicle.webtop.contacts', 'category.folder.default', "categories"."category_id"
FROM "contacts"."categories"
WHERE "categories"."is_default" IS TRUE
)
