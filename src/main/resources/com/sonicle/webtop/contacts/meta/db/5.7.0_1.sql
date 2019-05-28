@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update structure for categories
-- ----------------------------
ALTER TABLE "contacts"."contacts"
ADD COLUMN "display_name" varchar(255),
ADD COLUMN "company_master_data_id" varchar(36);

-- ----------------------------
-- Fix data for contacts
-- ----------------------------
UPDATE "contacts"."contacts" SET
"display_name" = TRIM(COALESCE("firstname", '') || ' ' || COALESCE("lastname", ''))
WHERE "is_list" IS FALSE;

UPDATE "contacts"."contacts" SET
"firstname" = "lastname",
"display_name" = "lastname"
WHERE "is_list" IS TRUE;

-- ----------------------------
-- Fix view setting values
-- ----------------------------
@DataSource[default@com.sonicle.webtop.core]
DELETE FROM "core"."user_settings" WHERE ("user_settings"."service_id" = 'com.sonicle.webtop.contacts') AND ("user_settings"."key" = 'showby');
DELETE FROM "core"."domain_settings" WHERE ("domain_settings"."service_id" = 'com.sonicle.webtop.contacts') AND ("domain_settings"."key" = 'showby');
DELETE FROM "core"."settings" WHERE ("settings"."service_id" = 'com.sonicle.webtop.contacts') AND ("settings"."key" = 'showby');
