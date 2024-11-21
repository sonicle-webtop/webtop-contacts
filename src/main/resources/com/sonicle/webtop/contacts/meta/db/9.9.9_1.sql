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
