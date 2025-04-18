@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Drop Foreign Keys leveraging on contact_id
-- ----------------------------
ALTER TABLE "contacts"."list_recipients" DROP CONSTRAINT IF EXISTS "list_recipients_contact_id_fkey";
ALTER TABLE "contacts"."list_recipients" DROP CONSTRAINT IF EXISTS "list_recipients_recipient_contact_id_fkey";
ALTER TABLE "contacts"."contacts_vcards" DROP CONSTRAINT IF EXISTS "contacts_vcards_contact_id_fkey";
ALTER TABLE "contacts"."contacts_pictures" DROP CONSTRAINT IF EXISTS "contacts_pictures_contact_id_fkey";
ALTER TABLE "contacts"."contacts_custom_values" DROP CONSTRAINT IF EXISTS "contacts_custom_values_contact_id_fkey";
ALTER TABLE "contacts"."contacts_attachments" DROP CONSTRAINT IF EXISTS "contacts_attachments_contact_id_fkey";

-- ----------------------------
-- Update contact_id field type
-- ----------------------------
ALTER TABLE "contacts"."contacts" ALTER COLUMN "contact_id" TYPE varchar(32) USING "contact_id"::varchar(32);
ALTER TABLE "contacts"."contacts_attachments" ALTER COLUMN "contact_id" TYPE varchar(32) USING "contact_id"::varchar(32);
ALTER TABLE "contacts"."contacts_custom_values" ALTER COLUMN "contact_id" TYPE varchar(32) USING "contact_id"::varchar(32);
ALTER TABLE "contacts"."contacts_pictures" ALTER COLUMN "contact_id" TYPE varchar(32) USING "contact_id"::varchar(32);
ALTER TABLE "contacts"."contacts_tags" ALTER COLUMN "contact_id" TYPE varchar(32) USING "contact_id"::varchar(32);
ALTER TABLE "contacts"."contacts_vcards" ALTER COLUMN "contact_id" TYPE varchar(32) USING "contact_id"::varchar(32);
ALTER TABLE "contacts"."list_recipients" ALTER COLUMN "contact_id" TYPE varchar(32) USING "contact_id"::varchar(32);
ALTER TABLE "contacts"."list_recipients" ALTER COLUMN "recipient_contact_id" TYPE varchar(32) USING "recipient_contact_id"::varchar(32);

-- ----------------------------
-- Update list_recipient_id field type
-- ----------------------------
ALTER TABLE "contacts"."list_recipients" ALTER COLUMN "list_recipient_id" TYPE varchar(32) USING "list_recipient_id"::varchar(32);

-- ----------------------------
-- Restore Foreign Keys leveraging on contact_id
-- ----------------------------
ALTER TABLE "contacts"."contacts_attachments" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "contacts"."contacts_custom_values" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "contacts"."contacts_pictures" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "contacts"."contacts_tags" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "contacts"."contacts_vcards" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "contacts"."list_recipients" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "contacts"."list_recipients" ADD FOREIGN KEY ("recipient_contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE SET NULL ON UPDATE CASCADE;

-- ----------------------------
-- Add revision_timestamp and creation_timestamp fields
-- ----------------------------
ALTER TABLE "contacts"."categories" 
ADD COLUMN "revision_timestamp" timestamptz,
ADD COLUMN "creation_timestamp" timestamptz;

UPDATE "contacts"."categories" SET
"revision_timestamp" = '1970-01-01 00:00:00+00'::timestamptz,
"creation_timestamp" = '1970-01-01 00:00:00+00'::timestamptz
WHERE "revision_timestamp" IS NULL;

ALTER TABLE "contacts"."categories" 
ALTER COLUMN "revision_timestamp" SET NOT NULL,
ALTER COLUMN "revision_timestamp" SET DEFAULT now(),
ALTER COLUMN "creation_timestamp" SET NOT NULL,
ALTER COLUMN "creation_timestamp" SET DEFAULT now();

-- ----------------------------
-- New table: history_categories
-- ----------------------------
DROP TABLE IF EXISTS "contacts"."history_categories";
DROP SEQUENCE IF EXISTS "contacts"."seq_history_categories";

CREATE SEQUENCE "contacts"."seq_history_categories";
CREATE TABLE "contacts"."history_categories" (
"id" int8 NOT NULL DEFAULT nextval('"contacts".seq_history_categories'::regclass),
"domain_id" varchar(20) NOT NULL,
"user_id" varchar(100) NOT NULL,
"category_id" int4 NOT NULL,
"change_timestamp" timestamptz NOT NULL DEFAULT '1970-01-01 00:00:00+00'::timestamptz,
"change_type" char(1) NOT NULL
);

ALTER TABLE "contacts"."history_categories" ADD PRIMARY KEY ("id");
CREATE INDEX "history_categories_ak1" ON "contacts"."history_categories" USING btree ("category_id", "change_timestamp");
CREATE INDEX "history_categories_ak2" ON "contacts"."history_categories" USING btree ("domain_id", "user_id", "category_id", "change_timestamp");

-- ----------------------------
-- New table: history_contacts
-- ----------------------------
DROP TABLE IF EXISTS "contacts"."history_contacts";
DROP SEQUENCE IF EXISTS "contacts"."seq_history_contacts";

CREATE SEQUENCE "contacts"."seq_history_contacts";
CREATE TABLE "contacts"."history_contacts" (
"id" int8 NOT NULL DEFAULT nextval('"contacts".seq_history_contacts'::regclass),
"category_id" int4 NOT NULL,
"contact_id" varchar(32) NOT NULL,
"change_timestamp" timestamptz NOT NULL DEFAULT '1970-01-01 00:00:00+00'::timestamptz,
"change_type" char(1) NOT NULL
);

ALTER TABLE "contacts"."history_contacts" ADD PRIMARY KEY ("id");
CREATE INDEX "history_contacts_ak1" ON "contacts"."history_contacts" USING btree ("category_id", "change_timestamp");
CREATE INDEX "history_contacts_ak2" ON "contacts"."history_contacts" USING btree ("contact_id", "change_timestamp");
