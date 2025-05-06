@DataSource[default@com.sonicle.webtop.contacts]

CREATE OR REPLACE FUNCTION "contacts"."update_contacts_history"()
  RETURNS TRIGGER AS $BODY$BEGIN
	IF TG_OP = 'DELETE' THEN
		INSERT INTO "contacts"."history_contacts" ("category_id", "contact_id", "change_timestamp", "change_type")
		VALUES (OLD."category_id", OLD."contact_id", NOW(), 'D');
		RETURN OLD;
	ELSIF TG_OP = 'INSERT' THEN
		INSERT INTO "contacts"."history_contacts" ("category_id", "contact_id", "change_timestamp", "change_type")
		VALUES (NEW."category_id", NEW."contact_id", NEW."revision_timestamp", 'C');
		RETURN NEW;
	ELSIF TG_OP = 'UPDATE' THEN
		IF NEW."category_id" <> OLD."category_id" THEN
			INSERT INTO "contacts"."history_contacts" ("category_id", "contact_id", "change_timestamp", "change_type")
			VALUES
			(OLD."category_id", NEW."contact_id", NEW."revision_timestamp", 'D'),
			(NEW."category_id", NEW."contact_id", NEW."revision_timestamp", 'C');
		ELSIF NEW."revision_status" <> OLD."revision_status" AND NEW."revision_status" = 'D' THEN
			INSERT INTO "contacts"."history_contacts" ("category_id", "contact_id", "change_timestamp", "change_type")
			VALUES (NEW."category_id", NEW."contact_id", NEW."revision_timestamp", 'D');
		ELSE
			INSERT INTO "contacts"."history_contacts" ("category_id", "contact_id", "change_timestamp", "change_type")
			VALUES (NEW."category_id", NEW."contact_id", NEW."revision_timestamp", 'U');
		END IF;
		RETURN NEW;
	END IF;
END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;