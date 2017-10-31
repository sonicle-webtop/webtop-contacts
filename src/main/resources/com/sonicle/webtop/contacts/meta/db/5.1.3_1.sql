@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Fix data for contacts
-- ----------------------------
UPDATE "contacts"."contacts" SET
"public_uid" = uuid_in(md5(random()::text || now()::text)::cstring)
WHERE "public_uid" IS NULL;

-- ----------------------------
-- Fix structure for contacts
-- ----------------------------
ALTER TABLE "contacts"."contacts"
ALTER COLUMN "public_uid" TYPE varchar(255);

ALTER TABLE "contacts"."contacts"
ALTER COLUMN "is_list" SET DEFAULT false;

ALTER TABLE "contacts"."contacts"
ALTER COLUMN "category_id" SET NOT NULL,
ALTER COLUMN "revision_status" SET NOT NULL,
ALTER COLUMN "revision_timestamp" SET NOT NULL,
ALTER COLUMN "public_uid" SET NOT NULL,
ALTER COLUMN "is_list" SET NOT NULL;
