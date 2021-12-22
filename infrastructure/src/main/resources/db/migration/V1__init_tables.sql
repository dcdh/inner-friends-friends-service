-- T_CONTACT_BOOK

CREATE TABLE public.T_FRIEND (
friendid character varying(255) NOT NULL,
friends jsonb NOT NULL,
bio character varying(1024) NOT NULL,
nboffriends bigint NOT NULL,
version bigint NOT NULL,
CONSTRAINT T_FRIEND_pkey PRIMARY KEY (friendid),
CONSTRAINT T_FRIEND_friendid_version_constraint UNIQUE (friendid, version)
);

ALTER TABLE public.T_FRIEND OWNER TO postgresql;

CREATE OR REPLACE FUNCTION friend_check_version_on_update()
  RETURNS trigger
  LANGUAGE PLPGSQL
  AS $$
DECLARE
  expected_version bigint := OLD.version + 1;
BEGIN
  IF NEW.friendid = OLD.friendid AND NEW.version != expected_version THEN
    Raise Exception 'Friend version unexpected on update for friendid % - current version % - expected version %', NEW.friendid, NEW.version, expected_version;
  END IF;
  RETURN NEW;
END;
$$;

CREATE TRIGGER friend_check_version_on_update_trigger
  BEFORE UPDATE
  ON public.T_FRIEND
  FOR EACH ROW
  EXECUTE PROCEDURE friend_check_version_on_update();

-- T_INVITATION_CODE_GENERATED

CREATE TABLE public.T_INVITATION_CODE_GENERATED (
invitationcode UUID NOT NULL,
fromfriendid character varying(255) NOT NULL,
generatedat timestamp without time zone,
CONSTRAINT T_INVITATION_CODE_GENERATED_pkey PRIMARY KEY (invitationcode)
);

ALTER TABLE public.T_INVITATION_CODE_GENERATED OWNER TO postgresql;
