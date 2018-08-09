@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------------------------
-- Add column to link contact with list recipient
-- ----------------------------------------------

ALTER TABLE "contacts"."list_recipients" ADD COLUMN "recipient_contact_id" int4;