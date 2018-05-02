@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update structure for contacts
-- ----------------------------
ALTER TABLE "contacts"."contacts" ADD COLUMN "creation_timestamp" timestamptz;

-- ----------------------------
-- Fix data for contacts
-- ----------------------------
UPDATE "contacts"."contacts" SET "creation_timestamp" = "revision_timestamp";

-- ----------------------------
-- Update structure for contacts
-- ----------------------------
ALTER TABLE "contacts"."contacts"
ALTER COLUMN "creation_timestamp" SET DEFAULT now(), 
ALTER COLUMN "creation_timestamp" SET NOT NULL;

-- ----------------------------
-- Table structure for contacts_vcards
-- ----------------------------
CREATE TABLE "contacts"."contacts_vcards" (
"contact_id" int4 NOT NULL,
"raw_data" text
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Primary Key structure for table contacts_vcards
-- ----------------------------
ALTER TABLE "contacts"."contacts_vcards" ADD PRIMARY KEY ("contact_id");
