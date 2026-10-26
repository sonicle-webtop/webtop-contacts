@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Restore history of live contacts wiped by the old history cleanup task
-- (they were missing from DAV full-syncs)
-- ----------------------------
INSERT INTO "contacts"."history_contacts" ("category_id", "contact_id", "change_timestamp", "change_type")
SELECT "contacts"."category_id", "contacts"."contact_id", NOW(), 'C'
FROM "contacts"."contacts"
WHERE "contacts"."revision_status" IN ('N','M')
AND NOT EXISTS (
SELECT 1 FROM "contacts"."history_contacts" AS "his"
WHERE "his"."contact_id" = "contacts"."contact_id" AND "his"."category_id" = "contacts"."category_id" AND "his"."change_type" IN ('C','U')
);
