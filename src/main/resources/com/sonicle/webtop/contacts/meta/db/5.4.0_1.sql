@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Update table: list_recipients
-- ----------------------------
ALTER TABLE "contacts"."list_recipients" ADD COLUMN "recipient_contact_id" int4;

-- ----------------------------
-- Add integrity constraints
-- ----------------------------
DELETE FROM "contacts"."contacts_pictures" WHERE "contact_id" NOT IN (SELECT "contacts"."contact_id" FROM "contacts"."contacts");
ALTER TABLE "contacts"."contacts_pictures" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
DELETE FROM "contacts"."contacts_vcards" WHERE "contact_id" NOT IN (SELECT "contacts"."contact_id" FROM "contacts"."contacts");
ALTER TABLE "contacts"."contacts_vcards" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
DELETE FROM "contacts"."list_recipients" WHERE "contact_id" NOT IN (SELECT "contacts"."contact_id" FROM "contacts"."contacts");
ALTER TABLE "contacts"."list_recipients" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "contacts"."list_recipients" ADD FOREIGN KEY ("recipient_contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE SET NULL ON UPDATE CASCADE;

-- ----------------------------
-- New table: contacts_attachments
-- ----------------------------
CREATE TABLE "contacts"."contacts_attachments" (
"contact_attachment_id" varchar(36) NOT NULL,
"contact_id" int4 NOT NULL,
"revision_timestamp" timestamptz(6) NOT NULL,
"revision_sequence" int2 NOT NULL,
"filename" varchar(255) NOT NULL,
"size" int8 NOT NULL,
"media_type" varchar(255) NOT NULL
)
WITH (OIDS=FALSE)

;

CREATE INDEX "contacts_attachments_ak1" ON "contacts"."contacts_attachments" USING btree ("contact_id");
ALTER TABLE "contacts"."contacts_attachments" ADD PRIMARY KEY ("contact_attachment_id");
ALTER TABLE "contacts"."contacts_attachments" ADD FOREIGN KEY ("contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE CASCADE ON UPDATE CASCADE;

-- ----------------------------
-- New table: contacts_attachments_data
-- ----------------------------
CREATE TABLE "contacts"."contacts_attachments_data" (
"contact_attachment_id" varchar(36) NOT NULL,
"bytes" bytea NOT NULL
)
WITH (OIDS=FALSE)

;

ALTER TABLE "contacts"."contacts_attachments_data" ADD PRIMARY KEY ("contact_attachment_id");
ALTER TABLE "contacts"."contacts_attachments_data" ADD FOREIGN KEY ("contact_attachment_id") REFERENCES "contacts"."contacts_attachments" ("contact_attachment_id") ON DELETE CASCADE ON UPDATE CASCADE;
