@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Migrate legacy color to the lightest one from default palette (tailwind)
-- ----------------------------
UPDATE "contacts"."categories" SET "color" = '#F3F4F6' WHERE "color" = '#FFFFFF';
UPDATE "contacts"."category_props" SET "color" = '#F3F4F6' WHERE "color" = '#FFFFFF';

-- ----------------------------
-- Make sure to have the FK in place on recipient_contact_id field
-- ----------------------------
UPDATE "contacts"."list_recipients" SET "recipient_contact_id" = NULL WHERE "recipient_contact_id" NOT IN (SELECT "contact_id" FROM "contacts"."contacts");
ALTER TABLE "contacts"."list_recipients" DROP CONSTRAINT IF EXISTS "list_recipients_recipient_contact_id_fkey";
ALTER TABLE "contacts"."list_recipients" ADD FOREIGN KEY ("recipient_contact_id") REFERENCES "contacts"."contacts" ("contact_id") ON DELETE SET NULL ON UPDATE CASCADE;
