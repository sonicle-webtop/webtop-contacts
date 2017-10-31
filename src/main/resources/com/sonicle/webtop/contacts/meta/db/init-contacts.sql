@DataSource[default@com.sonicle.webtop.contacts]

CREATE SCHEMA "contacts";

-- ----------------------------
-- Sequence structure for seq_categories
-- ----------------------------
DROP SEQUENCE IF EXISTS "contacts"."seq_categories";
CREATE SEQUENCE "contacts"."seq_categories";

-- ----------------------------
-- Sequence structure for seq_contacts
-- ----------------------------
DROP SEQUENCE IF EXISTS "contacts"."seq_contacts";
CREATE SEQUENCE "contacts"."seq_contacts";

-- ----------------------------
-- Sequence structure for seq_list_recipients
-- ----------------------------
DROP SEQUENCE IF EXISTS "contacts"."seq_list_recipients";
CREATE SEQUENCE "contacts"."seq_list_recipients";

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS "contacts"."categories";
CREATE TABLE "contacts"."categories" (
"category_id" int4 DEFAULT nextval('"contacts".seq_categories'::regclass) NOT NULL,
"domain_id" varchar(20) NOT NULL,
"user_id" varchar(100) NOT NULL,
"built_in" bool DEFAULT false NOT NULL,
"provider" varchar(20) DEFAULT 'local'::character varying NOT NULL,
"name" varchar(100) NOT NULL,
"description" varchar(255),
"color" varchar(20),
"sync" varchar(1) NOT NULL,
"is_default" bool DEFAULT false NOT NULL,
"parameters" text
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for contacts
-- ----------------------------
DROP TABLE IF EXISTS "contacts"."contacts";
CREATE TABLE "contacts"."contacts" (
"contact_id" int4 DEFAULT nextval('"contacts".seq_contacts'::regclass) NOT NULL,
"category_id" int4 NOT NULL,
"revision_status" varchar(1) NOT NULL,
"revision_timestamp" timestamptz(6) NOT NULL,
"revision_sequence" int4 DEFAULT 0 NOT NULL,
"public_uid" varchar(255) NOT NULL,
"is_list" bool DEFAULT false NOT NULL,
"searchfield" varchar(255),
"title" varchar(30),
"firstname" varchar(60),
"lastname" varchar(60),
"nickname" varchar(60),
"gender" varchar(6),
"company" varchar(60),
"function" varchar(50),
"work_address" varchar(100),
"work_city" varchar(50),
"work_state" varchar(30),
"work_postalcode" varchar(20),
"work_country" varchar(30),
"work_telephone" varchar(50),
"work_telephone2" varchar(50),
"work_fax" varchar(50),
"work_mobile" varchar(50),
"work_pager" varchar(50),
"work_email" varchar(320),
"work_im" varchar(200),
"assistant" varchar(30),
"assistant_telephone" varchar(50),
"department" varchar(200),
"manager" varchar(200),
"home_address" varchar(100),
"home_city" varchar(50),
"home_state" varchar(30),
"home_postalcode" varchar(20),
"home_country" varchar(30),
"home_telephone" varchar(50),
"home_telephone2" varchar(50),
"home_fax" varchar(50),
"home_mobile" varchar(50),
"home_pager" varchar(50),
"home_email" varchar(320),
"home_im" varchar(200),
"partner" varchar(200),
"birthday" date,
"anniversary" date,
"other_address" varchar(100),
"other_city" varchar(50),
"other_state" varchar(30),
"other_postalcode" varchar(20),
"other_country" varchar(30),
"other_email" varchar(320),
"other_im" varchar(200),
"url" varchar(2048),
"notes" varchar(2000),
"href" varchar(2048),
"etag" varchar(2048)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for contacts_pictures
-- ----------------------------
DROP TABLE IF EXISTS "contacts"."contacts_pictures";
CREATE TABLE "contacts"."contacts_pictures" (
"contact_id" int4 NOT NULL,
"width" int4,
"height" int4,
"media_type" varchar(50),
"bytes" bytea
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for list_recipients
-- ----------------------------
DROP TABLE IF EXISTS "contacts"."list_recipients";
CREATE TABLE "contacts"."list_recipients" (
"list_recipient_id" int4 DEFAULT nextval('"contacts".seq_list_recipients'::regclass) NOT NULL,
"contact_id" int4 NOT NULL,
"recipient" varchar(320) NOT NULL,
"recipient_type" varchar(3)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Indexes structure for table categories
-- ----------------------------
CREATE INDEX "categories_ak1" ON "contacts"."categories" USING btree ("domain_id", "user_id", "built_in");
CREATE UNIQUE INDEX "categories_ak2" ON "contacts"."categories" USING btree ("domain_id", "user_id", "name");

-- ----------------------------
-- Primary Key structure for table categories
-- ----------------------------
ALTER TABLE "contacts"."categories" ADD PRIMARY KEY ("category_id");

-- ----------------------------
-- Indexes structure for table contacts
-- ----------------------------
CREATE INDEX "contacts_ak1" ON "contacts"."contacts" USING btree ("category_id", "revision_status", "revision_timestamp");
CREATE INDEX "contacts_ak2" ON "contacts"."contacts" USING btree ("category_id", "revision_status", "lastname");
CREATE INDEX "contacts_ak3" ON "contacts"."contacts" USING btree ("category_id", "revision_status", "searchfield");
CREATE INDEX "contacts_ak4" ON "contacts"."contacts" USING btree ("revision_status", "birthday");
CREATE INDEX "contacts_ak5" ON "contacts"."contacts" USING btree ("revision_status", "anniversary");

-- ----------------------------
-- Primary Key structure for table contacts
-- ----------------------------
ALTER TABLE "contacts"."contacts" ADD PRIMARY KEY ("contact_id");

-- ----------------------------
-- Primary Key structure for table contacts_pictures
-- ----------------------------
ALTER TABLE "contacts"."contacts_pictures" ADD PRIMARY KEY ("contact_id");

-- ----------------------------
-- Primary Key structure for table list_recipients
-- ----------------------------
ALTER TABLE "contacts"."list_recipients" ADD PRIMARY KEY ("list_recipient_id");

-- ----------------------------
-- Align service version
-- ----------------------------
@DataSource[default@com.sonicle.webtop.core]
DELETE FROM "core"."settings" WHERE ("settings"."service_id" = 'com.sonicle.webtop.contacts') AND ("settings"."key" = 'manifest.version');
INSERT INTO "core"."settings" ("service_id", "key", "value") VALUES ('com.sonicle.webtop.contacts', 'manifest.version', '5.1.3');
