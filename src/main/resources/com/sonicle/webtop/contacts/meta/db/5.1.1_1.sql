@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Fix structure for categories
-- ----------------------------
ALTER TABLE "contacts"."categories"
ALTER COLUMN "provider" SET DEFAULT 'local'::character varying, ALTER COLUMN "provider" SET NOT NULL;
