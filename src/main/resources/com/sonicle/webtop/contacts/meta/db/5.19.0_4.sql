@DataSource[default@com.sonicle.webtop.contacts]

-- ----------------------------
-- Fill history_categories retrieving data from last-saved status
-- ----------------------------
INSERT INTO "contacts"."history_categories" ("domain_id", "user_id", "category_id", "change_type")
SELECT "categories"."domain_id", "categories"."user_id", "categories"."category_id", 'C'
FROM "contacts"."categories";

-- ----------------------------
-- Fill history_contacts retrieving data from last-saved status
-- ----------------------------
INSERT INTO "contacts"."history_contacts" ("category_id", "contact_id", "change_timestamp", "change_type")
SELECT "contacts"."category_id", "contacts"."contact_id", "contacts"."creation_timestamp", 'C'
FROM "contacts"."contacts"
INNER JOIN "contacts"."categories" ON "contacts"."category_id" = "categories"."category_id"
WHERE "contacts"."revision_status" IN ('N','M')
UNION
SELECT "contacts"."category_id", "contacts"."contact_id", "contacts"."revision_timestamp", 'U'
FROM "contacts"."contacts"
INNER JOIN "contacts"."categories" ON "contacts"."category_id" = "categories"."category_id"
WHERE "contacts"."revision_status" IN ('M');

INSERT INTO "contacts"."history_contacts" ("category_id", "contact_id", "change_timestamp", "change_type")
SELECT "contacts"."category_id", "contacts"."contact_id", "contacts"."creation_timestamp", 'D'
FROM "contacts"."contacts"
INNER JOIN "contacts"."categories" ON "contacts"."category_id" = "categories"."category_id"
WHERE "contacts"."revision_status" IN ('D');

-- ----------------------------
-- Add categories triggers
-- ----------------------------
DROP TRIGGER IF EXISTS "trg_categories_onafter_1" ON "contacts"."categories";
CREATE TRIGGER "trg_categories_onafter_1" AFTER INSERT OR UPDATE ON "contacts"."categories"
FOR EACH ROW
EXECUTE PROCEDURE "contacts"."update_categories_history"();

DROP TRIGGER IF EXISTS "trg_categories_onafter_2" ON "contacts"."categories";
CREATE TRIGGER "trg_categories_onafter_2" AFTER DELETE ON "contacts"."categories"
FOR EACH ROW
EXECUTE PROCEDURE "contacts"."update_categories_history"();

-- ----------------------------
-- Add contacts triggers
-- ----------------------------
DROP TRIGGER IF EXISTS "trg_contacts_onafter_1" ON "contacts"."contacts";
CREATE TRIGGER "trg_contacts_onafter_1" AFTER INSERT OR DELETE ON "contacts"."contacts"
FOR EACH ROW
EXECUTE PROCEDURE "contacts"."update_contacts_history"();

DROP TRIGGER IF EXISTS "trg_contacts_onafter_2" ON "contacts"."contacts";
CREATE TRIGGER "trg_contacts_onafter_2" AFTER UPDATE OF "revision_timestamp" ON "contacts"."contacts"
FOR EACH ROW
EXECUTE PROCEDURE "contacts"."update_contacts_history"();

