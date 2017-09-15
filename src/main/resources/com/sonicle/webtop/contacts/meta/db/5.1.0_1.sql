@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update structure for categories
-- ----------------------------
ALTER TABLE "contacts"."categories"
ALTER COLUMN "built_in" SET DEFAULT false,
ALTER COLUMN "is_default" SET DEFAULT false;

ALTER TABLE "contacts"."categories"
ADD COLUMN "provider" varchar(20),
ADD COLUMN "parameters" text;

-- ----------------------------
-- Fix data
-- ----------------------------
UPDATE "contacts"."categories" SET "provider" = 'local';

-- ----------------------------
-- Update structure for calendars
-- ----------------------------
ALTER TABLE "contacts"."categories" ALTER COLUMN "provider" SET NOT NULL;

-- ----------------------------
-- Update structure for events
-- ----------------------------
ALTER TABLE "contacts"."contacts"
ALTER COLUMN "url" TYPE varchar(2048);

ALTER TABLE "contacts"."contacts"
ADD COLUMN "href" varchar(2048),
ADD COLUMN "etag" varchar(2048);
