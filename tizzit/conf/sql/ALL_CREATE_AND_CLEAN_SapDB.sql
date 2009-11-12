CREATE TABLE COMP_ADDRESS (
	"ADDRESS_ID" DECIMAL(20) NOT NULL, 
	"ROOM_NR" VARCHAR(255) NULL, 
	"BUILDING_LEVEL" VARCHAR(255) NULL, 
	"BUILDING_NR" VARCHAR(255) NULL, 
	"STREET" VARCHAR(255) NULL, 
	"STREET_NR" VARCHAR(255) NULL, 
	"POST_OFFICE_BOX" VARCHAR(255) NULL, 
	"COUNTRY_CODE" VARCHAR(3) NULL, 
	"COUNTRY" VARCHAR(255) NULL, 
	"ZIP_CODE" VARCHAR(50) NULL, 
	"CITY" VARCHAR(255) NULL, 
	"PHONE1" VARCHAR(255) NULL, 
	"PHONE2" VARCHAR(255) NULL, 
	"FAX" VARCHAR(255) NULL, 
	"MOBILE_PHONE" VARCHAR(255) NULL, 
	"EMAIL" VARCHAR(255) NULL, 
	"HOMEPAGE" VARCHAR(255) NULL, 
	"ADDRESS_TYPE" VARCHAR(255) DEFAULT 'Büro' NULL, 
	"MISC" LONG VARCHAR NULL, 
	"PERSON_ID_FK" DECIMAL(20) NULL, 
	"UNIT_ID_FK" INTEGER NULL, 
	"DEPARTMENT_ID_FK" DECIMAL(20) NULL, 
	"EXTERNAL_ID" VARCHAR(255) NULL, 
	"LAST_MODIFIED_DATE" DECIMAL(20) DEFAULT '0' NULL,
	PRIMARY KEY (address_id))
//
CREATE INDEX idx_comp_address_person_id_fk ON comp_address (person_id_fk)
//
CREATE INDEX idx_comp_address_unit_id_fk ON comp_address (unit_id_fk)
//
CREATE INDEX idx_comp_address_department_id ON comp_address (department_id_fk)
//
CREATE INDEX idx_comp_address_external_id ON comp_address (external_id)
//
CREATE INDEX idx_comp_address_last_modified ON comp_address (last_modified_date)
//
CREATE TABLE COMP_DEPARTMENT (
	"DEPARTMENT_ID" DECIMAL(20) NOT NULL, 
	"NAME" VARCHAR(255) NULL, 
	"UNIT_ID_FK" DECIMAL(20) NULL,
	PRIMARY KEY (department_id))
//
CREATE INDEX idx_comp_department_unit_id_fk ON comp_department (unit_id_fk)
//
CREATE TABLE COMP_PERSON (
	"PERSON_ID" DECIMAL(20) NOT NULL, 
	"BIRTH_DAY" VARCHAR(50) NULL, 
	"POSITION" INTEGER NULL, 
	"SALUTATION" VARCHAR(50) NULL, 
	"SEX" SMALLINT NULL, 
	"TITLE" VARCHAR(255) NULL, 
	"FIRSTNAME" VARCHAR(255) NULL, 
	"LASTNAME" VARCHAR(255) NULL, 
	"JOB" VARCHAR(100) NULL, 
	"IMAGE_ID" INTEGER NULL, 
	"JOB_TITLE" VARCHAR(255) NULL, 
	"COUNTRY_JOB" VARCHAR(100) NULL, 
	"MEDICAL_ASSOCIATION" VARCHAR(255) NULL, 
	"LINK_MEDICAL_ASSOCIATION" VARCHAR(255) NULL, 
	"EXTERNAL_ID" VARCHAR(255) NULL, 
	"LAST_MODIFIED_DATE" DECIMAL(20) DEFAULT '0' NULL,
	PRIMARY KEY (person_id))
//
CREATE INDEX idx_comp_person_image_id ON comp_person (image_id)
//
CREATE INDEX idx_comp_person_external_id ON comp_person (external_id)
//
CREATE INDEX idx_comp_person_last_modified_ ON comp_person (last_modified_date)
//
CREATE TABLE COMP_PERSONTOUNITLINK (
	"PERSONTOUNITLINK_ID" INTEGER NOT NULL, 
	"ROLE_TYPE" INTEGER NULL, 
	"UNIT_ID_FK" INTEGER NULL, 
	"PERSON_ID_FK" DECIMAL(20) NULL,
	PRIMARY KEY (personToUnitLink_id))
//
CREATE INDEX idx_comp_persontounitlink_unit ON comp_persontounitlink (unit_id_fk)
//
CREATE INDEX idx_comp_persontounitlink_pers ON comp_persontounitlink (person_id_fk)
//
CREATE TABLE COMP_TALKTIME (
	"TALK_TIME_ID" DECIMAL(20) NOT NULL, 
	"TALK_TIME_TYPE" VARCHAR(255) NULL, 
	"TALK_TIMES" LONG VARCHAR NULL, 
	"PERSON_ID_FK" DECIMAL(20) NULL, 
	"UNIT_ID_FK" INTEGER NULL, 
	"DEPARTMENT_ID_FK" DECIMAL(20) NULL,
	PRIMARY KEY (talk_time_id))
//
CREATE INDEX idx_comp_talktime_person_id_fk ON comp_talktime (person_id_fk)
//
CREATE INDEX idx_comp_talktime_unit_id_fk ON comp_talktime (unit_id_fk)
//
CREATE INDEX idx_comp_talktime_department_i ON comp_talktime (department_id_fk)
//
CREATE TABLE CONTENT (
	"CONTENT_ID" INTEGER NOT NULL, 
	"STATUS" INTEGER NULL, 
	"TEMPLATE" VARCHAR(100) NULL,
	"UPDATE_SEARCH_INDEX" SMALLINT DEFAULT '1' NULL, 
	PRIMARY KEY (content_id))
//
CREATE TABLE CONTENTVERSION (
	"CONTENT_VERSION_ID" INTEGER NOT NULL, 
	"VERSION" VARCHAR(5) NULL, 
	"HEADING" VARCHAR(255) NULL, 
	"TEXT" LONG VARCHAR NULL, 
	"VERSION_COMMENT" LONG VARCHAR NULL, 
	"CREATE_DATE" DECIMAL(20) NULL, 
	"CREATOR" VARCHAR(255) NULL, 
	"CONTENT_ID_FK" INTEGER NULL, 
	"LOCK_ID_FK" INTEGER NULL,
	PRIMARY KEY (content_version_id))
//
CREATE INDEX idx_contentversion_version ON contentversion (version)
//
CREATE INDEX idx_contentversion_create_date ON contentversion (create_date)
//
CREATE INDEX idx_contentversion_creator ON contentversion (creator)
//
CREATE INDEX idx_contentversion_content_id_ ON contentversion (content_id_fk)
//
CREATE INDEX idx_contentversion_lock_id_fk ON contentversion (lock_id_fk)
//
CREATE TABLE DEPARTMENTS2PERSONS (
	"PERSONS_ID_FK" DECIMAL(20) NOT NULL, 
	"DEPARTMENTS_ID_FK" DECIMAL(20) NOT NULL,
	PRIMARY KEY (persons_id_fk, departments_id_fk))
//
CREATE TABLE DOCUMENT (
	"DOCUMENT_ID" INTEGER NOT NULL, 
	"DOCUMENT" LONG BYTE NULL, 
	"DOCUMENT_NAME" VARCHAR(255) NULL, 
	"TIME_STAMP" DECIMAL(20) NULL, 
	"MIME_TYPE" VARCHAR(50) NULL, 
	"UNIT_ID_FK" INTEGER NULL, 
	"USE_COUNT_LAST_VERSION" INTEGER NULL, 
	"USE_COUNT_PUBLISH_VERSION" INTEGER NULL,
	"UPDATE_SEARCH_INDEX" SMALLINT DEFAULT '1' NULL, 
	PRIMARY KEY (document_id))
//
CREATE INDEX idx_document_unit_id_fk ON document (unit_id_fk)
//
CREATE TABLE EDITION (
	"EDITION_ID" INTEGER NOT NULL, 
	"CREATOR_ID_FK" VARCHAR(255) NULL, 
	"USER_COMMENT" LONG VARCHAR NULL, 
	"CREATION_DATE" DECIMAL(20) NULL, 
	"STATUS" SMALLINT NULL, 
	"UNIT_ID" INTEGER NULL, 
	"VIEW_DOCUMENT_ID" INTEGER NULL,
	"WORK_SERVER_EDITION_ID" INTEGER NULL,
	"DEPLOY_STATUS" VARCHAR(255),
	"START_ACTION_TIMESTAMP" DECIMAL(20),
	"END_ACTION_TIMESTAMP" DECIMAL(20),
	PRIMARY KEY (edition_id))
//
CREATE INDEX idx_edition_creator_id_fk ON edition (creator_id_fk)
//
CREATE INDEX idx_edition_unit_id ON edition (unit_id)
//
CREATE INDEX idx_edition_view_document_id ON edition (view_document_id)
//
CREATE TABLE EDITIONSLICE (
	"EDITION_SLICE_ID" INTEGER NOT NULL, 
	"EDITION_ID" INTEGER NOT NULL, 
	"SLICE_NR" INTEGER NOT NULL, 
	"SLICE_DATA" LONG BYTE NULL,
	PRIMARY KEY (edition_slice_id))
//
CREATE INDEX idx_editionslice_edition_id ON editionslice (edition_id)
//
CREATE INDEX idx_editionslice_slice_nr ON editionslice (slice_nr)
//
CREATE TABLE GROUPS2ROLES (
	"ROLES_ID_FK" VARCHAR(255) NOT NULL, 
	"GROUPS_ID_FK" INTEGER NOT NULL,
	PRIMARY KEY (roles_id_fk, groups_id_fk))
//
CREATE TABLE GROUPS2USERS (
	"USERS_ID_FK" VARCHAR(255) NOT NULL, 
	"GROUPS_ID_FK" INTEGER NOT NULL,
	PRIMARY KEY (users_id_fk, groups_id_fk))
//
CREATE TABLE HOST (
	"HOST_NAME" VARCHAR(255) NOT NULL, 
	"LIVESERVER" SMALLINT DEFAULT '0' NULL,
	"SITE_ID_FK" INTEGER NULL, 
	"STARTPAGE_VC_ID_FK" INTEGER NULL, 
	"UNIT_ID_FK" INTEGER NULL, 
	"REDIRECT_URL" VARCHAR(255) NULL, 
	"REDIRECT_HOST_NAME_ID_FK" VARCHAR(255) NULL,
	PRIMARY KEY (host_name))
//
CREATE INDEX idx_host_site_id_fk ON host (site_id_fk)
//
CREATE INDEX idx_host_startpage_vc_id_fk ON host (startpage_vc_id_fk)
//
CREATE INDEX idx_host_unit_id_fk ON host (unit_id_fk)
//
CREATE INDEX idx_host_redirect_host_name_id ON host (redirect_host_name_id_fk)
//
CREATE TABLE KEYGEN (
	"IDX" INTEGER NOT NULL, 
	"NAME" VARCHAR(100) NOT NULL,
	PRIMARY KEY (name))
//
CREATE TABLE LOCKS (
	"LOCK_ID" INTEGER NOT NULL, 
	"CREATE_DATE" DECIMAL(20) NULL, 
	"OWNER_ID_FK" VARCHAR(255) NULL,
	PRIMARY KEY (lock_id))
//
CREATE INDEX idx_locks_owner_id_fk ON locks (owner_id_fk)
//
CREATE TABLE PERSONS2UNITS (
	"PERSONS_ID_FK" DECIMAL(20) NOT NULL, 
	"UNITS_ID_FK" INTEGER NOT NULL,
	PRIMARY KEY (persons_id_fk, units_id_fk))
//
CREATE TABLE PICTURE (
	"PICTURE_ID" INTEGER NOT NULL, 
	"THUMBNAIL" LONG BYTE NULL, 
	"PICTURE" LONG BYTE NULL, 
	"PREVIEW" LONG BYTE NULL, 
	"TIME_STAMP" DECIMAL(20) NULL, 
	"MIME_TYPE" VARCHAR(50) NULL, 
	"UNIT_ID_FK" INTEGER NULL, 
	"ALT_TEXT" VARCHAR(255) NULL, 
	"PICTURE_NAME" VARCHAR(255) NULL, 
	"HEIGHT" INTEGER NULL, 
	"WIDTH" INTEGER NULL,
	PRIMARY KEY (picture_id))
//
CREATE INDEX idx_picture_unit_id_fk ON picture (unit_id_fk)
//
CREATE TABLE ROLE (
	"ROLE_ID" VARCHAR(255) NOT NULL,
	PRIMARY KEY (role_id))
//
CREATE TABLE SGROUP (
	"GROUP_ID" INTEGER NOT NULL, 
	"GROUP_NAME" VARCHAR(255) NULL, 
	"SITE_ID_FK" INTEGER NULL,
	PRIMARY KEY (group_id))
//
CREATE INDEX idx_sgroup_site_id_fk ON sgroup (site_id_fk)
//
CREATE TABLE SITE (
	"SITE_ID" INTEGER NOT NULL, 
	"SITE_NAME" VARCHAR(255) NULL, 
	"SITE_SHORT" VARCHAR(50) NULL, 
	"SITE_CONFIG" LONG VARCHAR NULL, 
	"MANDATOR_DIR" VARCHAR(255) NULL, 
	"ROOT_UNIT_ID_FK" INTEGER NULL, 
	"DEFAULT_VIEW_DOCUMENT_ID_FK" INTEGER NULL, 
	"CACHE_EXPIRE" DECIMAL(20) NULL, 
	"SITE_GROUP_ID_FK" INTEGER NULL, 
	"WYSIWYG_IMAGE_URL" VARCHAR(255) NULL, 
	"HELP_URL" VARCHAR(255) NULL, 
	"DCF_URL" VARCHAR(255) NULL, 
	"PREVIEW_URL" VARCHAR(255) NULL, 
	"PAGE_NAME_FULL" VARCHAR(255) NULL, 
	"PAGE_NAME_CONTENT" VARCHAR(255) NULL, 
	"PAGE_NAME_SEARCH" VARCHAR(255) NULL, 
	"LAST_MODIFIED_DATE" DECIMAL(20) DEFAULT '0' NULL,
	PRIMARY KEY (site_id))
//
CREATE INDEX idx_site_site_short ON site (site_short)
//
CREATE INDEX idx_site_mandator_dir ON site (mandator_dir)
//
CREATE INDEX idx_site_last_modified_date ON site (last_modified_date)
//
CREATE TABLE SITE_GROUP (
	"SITE_GROUP_ID" INTEGER NOT NULL, 
	"SITE_GROUP_NAME" VARCHAR(255) NULL,
	PRIMARY KEY (site_group_id))
//
CREATE TABLE SITES2USERS (
	"USERS_ID_FK" VARCHAR(255) NOT NULL, 
	"SITES_ID_FK" INTEGER NOT NULL,
	PRIMARY KEY (users_id_fk, sites_id_fk))
//
CREATE TABLE TASK (
	"TASK_ID" INTEGER NOT NULL, 
	"TASK_TYPE" SMALLINT NULL, 
	"RECEIVER_ROLE" VARCHAR(50) NULL, 
	"UNIT_ID_FK" INTEGER NULL, 
	"RECEIVER" VARCHAR(255) NULL, 
	"SENDER" VARCHAR(255) NULL, 
	"USER_COMMENT" LONG VARCHAR NULL, 
	"STATUS" SMALLINT NULL, 
	"CREATION_DATE" DECIMAL(20) NULL,
	PRIMARY KEY (task_id))
//
CREATE INDEX idx_task_receiver_role ON task (receiver_role)
//
CREATE INDEX idx_task_unit_id_fk ON task (unit_id_fk)
//
CREATE INDEX idx_task_receiver ON task (receiver)
//
CREATE INDEX idx_task_sender ON task (sender)
//
CREATE TABLE TASK2VIEW_COMPONENTS (
	"TASK_ID_FK" INTEGER NOT NULL, 
	"VIEW_COMPONENTS_ID_FK" INTEGER NOT NULL,
	PRIMARY KEY (task_id_fk, view_components_id_fk))
//
CREATE TABLE UNIT (
	"UNIT_ID" INTEGER NOT NULL, 
	"NAME" VARCHAR(255) NULL, 
	"IMAGE_ID" INTEGER NULL, 
	"LOGO_ID" INTEGER NULL, 
	"SITE_ID_FK" INTEGER NULL, 
	"LAST_MODIFIED_DATE" DECIMAL(20) DEFAULT '0' NULL,
	PRIMARY KEY (unit_id))
//
CREATE INDEX idx_unit_image_id ON unit (image_id)
//
CREATE INDEX idx_unit_logo_id ON unit (logo_id)
//
CREATE INDEX idx_unit_site_id_fk ON unit (site_id_fk)
//
CREATE INDEX idx_unit_last_modified_date ON unit (last_modified_date)
//
CREATE TABLE UNITS2USERS (
	"USERS_ID_FK" VARCHAR(255) NOT NULL, 
	"UNITS_ID_FK" INTEGER NOT NULL,
	PRIMARY KEY (users_id_fk, units_id_fk))
//
CREATE TABLE USR (
	"USER_ID" VARCHAR(255) NOT NULL, 
	"FIRST_NAME" VARCHAR(255) NULL, 
	"LAST_NAME" VARCHAR(255) NULL, 
	"EMAIL" VARCHAR(255) NULL, 
	"CONFIG_XML" LONG VARCHAR NULL, 
	"LOGIN_DATE" DECIMAL(20) NULL, 
	"ACTIVE_SITE_ID_FK" INTEGER NULL, 
	"MASTERROOT" SMALLINT DEFAULT '0' NULL, 
	"PASSWD" VARCHAR(60) NULL,
	PRIMARY KEY (user_id))
//
CREATE INDEX idx_usr_active_site_id_fk ON usr (active_site_id_fk)
//
CREATE TABLE VIEWCOMPONENT (
	"VIEW_COMPONENT_ID" INTEGER NOT NULL, 
	"PARENT_ID_FK" INTEGER NULL, 
	"FIRST_CHILD_ID_FK" INTEGER NULL, 
	"PREV_NODE_ID_FK" INTEGER NULL, 
	"NEXT_NODE_ID_FK" INTEGER NULL, 
	"UNIT_ID_FK" INTEGER NULL, 
	"STATUS" INTEGER NULL, 
	"SHOW_TYPE" SMALLINT NULL, 
	"VIEW_TYPE" SMALLINT NULL, 
	"VISIBLE" SMALLINT NULL, 
	"SEARCH_INDEXED" SMALLINT NULL, 
	"XML_SEARCH_INDEXED" SMALLINT DEFAULT '1' NULL, 
	"DISPLAY_LINK_NAME" VARCHAR(255) NULL, 
	"LINK_DESCRIPTION" VARCHAR(255) NULL, 
	"URL_LINK_NAME" VARCHAR(255) NULL, 
	"APPROVED_LINK_NAME" VARCHAR(255) NULL, 
	"VIEW_LEVEL" VARCHAR(10) NULL, 
	"VIEW_INDEX" VARCHAR(10) NULL, 
	"ONLINE_START" DECIMAL(20) NULL, 
	"ONLINE_STOP" DECIMAL(20) NULL, 
	"REFERENCE" VARCHAR(255) NULL, 
	"VIEW_DOCUMENT_ID_FK" INTEGER NULL, 
	"ONLINE_STATE" SMALLINT DEFAULT '0' NULL, 
	"DEPLOY_COMMAND" SMALLINT DEFAULT '0' NULL, 
	"META_DATA" LONG VARCHAR NULL, 
	"META_DESCRIPTION" LONG VARCHAR NULL, 
	"CREATE_DATE" DECIMAL(20) DEFAULT '0' NULL, 
	"LAST_MODIFIED_DATE" DECIMAL(20) DEFAULT '0' NULL, 
	"REALM2VC_ID_FK" INTEGER NULL, 
	"DISPLAY_SETTINGS" SMALLINT DEFAULT '0' NULL, 
	"USER_LAST_MODIFIED_DATE" DECIMAL(20) DEFAULT '0' NULL,
	PRIMARY KEY (view_component_id))
//
CREATE INDEX idx_viewcomponent_parent_id_fk ON viewcomponent (parent_id_fk)
//
CREATE INDEX idx_viewcomponent_first_child_ ON viewcomponent (first_child_id_fk)
//
CREATE INDEX idx_viewcomponent_prev_node_id ON viewcomponent (prev_node_id_fk)
//
CREATE INDEX idx_viewcomponent_next_node_id ON viewcomponent (next_node_id_fk)
//
CREATE INDEX idx_viewcomponent_unit_id_fk ON viewcomponent (unit_id_fk)
//
CREATE INDEX idx_viewcomponent_display_link ON viewcomponent (display_link_name)
//
CREATE INDEX idx_viewcomponent_reference ON viewcomponent (reference)
//
CREATE INDEX idx_viewcomponent_view_documen ON viewcomponent (view_document_id_fk)
//
CREATE INDEX idx_viewcomponent_realm2vc_id_ ON viewcomponent (realm2vc_id_fk)
//
CREATE TABLE VIEWDOCUMENT (
	"VIEW_DOCUMENT_ID" INTEGER NOT NULL, 
	"LANGUAGE" VARCHAR(50) NULL, 
	"VIEW_TYPE" VARCHAR(50) NULL, 
	"VIEW_COMPONENT_ID_FK" INTEGER NULL, 
	"SITE_ID_FK" INTEGER NULL,
	PRIMARY KEY (view_document_id))
//
CREATE INDEX idx_viewdocument_language ON viewdocument (language)
//
CREATE INDEX idx_viewdocument_view_type ON viewdocument (view_type)
//
CREATE INDEX idx_viewdocument_view_componen ON viewdocument (view_component_id_fk)
//
CREATE INDEX idx_viewdocument_site_id_fk ON viewdocument (site_id_fk)
//
CREATE TABLE REALM_LDAP (
	"LDAP_REALM_ID" INTEGER NOT NULL, 
	"REALM_NAME" VARCHAR(255) NULL, 
	"LDAP_URL" VARCHAR(255) NULL, 
	"LDAP_AUTHENTICATION_TYPE" VARCHAR(255) NULL, 
	"LDAP_SUFFIX" VARCHAR(255) NULL, 
	"LDAP_PREFIX" VARCHAR(255) NULL, 
	"LOGIN_PAGE_ID" VARCHAR(255) NULL, 
	"SITE_ID_FK" INTEGER NULL,
	PRIMARY KEY (ldap_realm_id))
//
CREATE TABLE REALM_SIMPLE_PW (
	"SIMPLE_PW_REALM_ID" INTEGER NOT NULL, 
	"REALM_NAME" VARCHAR(255) NULL, 
	"LOGIN_PAGE_ID" VARCHAR(255) NULL, 
	"OWNER_ID_FK" VARCHAR(255) NULL, 
	"SITE_ID_FK" INTEGER NULL,
	PRIMARY KEY (simple_pw_realm_id))
//
CREATE TABLE REALM_SIMPLE_PW_USER (
	"SIMPLE_PW_REALM_USER_ID" INTEGER NOT NULL, 
	"USER_NAME" VARCHAR(255) NULL, 
	"PASSWORD" VARCHAR(255) NULL, 
	"ROLES" VARCHAR(1024) NULL, 
	"SIMPLE_PW_REALM_ID_FK" INTEGER NULL,
	PRIMARY KEY (simple_pw_realm_user_id))
//
CREATE TABLE REALM_JDBC (
	"JDBC_REALM_ID" INTEGER NOT NULL, 
	"STATEMENT_USER" VARCHAR(255) NULL, 
	"STATEMENT_ROLE_PER_USER" VARCHAR(255) NULL, 
	"JNDI_NAME" VARCHAR(255) NULL, 
	"REALM_NAME" VARCHAR(255) NULL, 
	"LOGIN_PAGE_ID" VARCHAR(255) NULL, 
	"SITE_ID_FK" INTEGER NULL,
	PRIMARY KEY (jdbc_realm_id))
//
CREATE TABLE REALM_JAAS (
	"JAAS_REALM_ID" INTEGER NOT NULL, 
	"REALM_NAME" VARCHAR(255) NULL, 
	"LOGIN_PAGE_ID" VARCHAR(255) NULL, 
	"JAAS_POLICY_NAME" VARCHAR(255) NULL, 
	"SITE_ID_FK" INTEGER NULL,
	PRIMARY KEY (jaas_realm_id))
//
CREATE TABLE REALM2VIEW_COMPONENT (
	"REALM2VIEW_COMPONENT_ID" INTEGER NOT NULL, 
	"ROLE_NEEDED" VARCHAR(255) NULL, 
	"SIMPLE_PW_REALM_ID_FK" INTEGER NULL, 
	"JDBC_REALM_ID_FK" INTEGER NULL, 
	"LDAP_REALM_ID_FK" INTEGER NULL, 
	"JAAS_REALM_ID_FK" INTEGER NULL, 
	"VIEW_COMPONENT_ID_FK" INTEGER NULL, 
	"LOGIN_PAGE_ID_FK" INTEGER NULL,
	PRIMARY KEY (realm2view_component_id))
//
CREATE TABLE SHORT_LINK (
	"SHORT_LINK_ID" INTEGER NOT NULL, 
	"SHORT_LINK" VARCHAR(255) NULL, 
	"REDIRECT_URL" VARCHAR(255) NULL, 
	"SITE_ID_FK" INTEGER NULL, 
	"VIEW_DOCUMENT_ID_FK" INTEGER NULL,
	PRIMARY KEY (short_link_id))
//
CREATE INDEX idx_short_link_site_id_fk ON short_link (site_id_fk)
//
CREATE INDEX idx_short_link_view_document_i ON short_link (view_document_id_fk)
//
DELETE FROM keygen;

INSERT INTO keygen (idx, name) VALUES ('1', 'address.address_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'content.content_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'contentversion.content_version_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'department.department_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'document.document_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'edition.edition_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'editionslice.edition_slice_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'locks.lock_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'person.person_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'picture.picture_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'talktime.talk_time_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'task.task_id')
//
INSERT INTO keygen (idx, name) VALUES ('2', 'unit.unit_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'viewcomponent.view_component_id')
//
INSERT INTO keygen (idx, name) VALUES ('1', 'viewdocument.view_document_id')
//
INSERT INTO keygen (idx, name) VALUES ('2', 'site.site_id')
//
INSERT INTO role (role_id) VALUES ('changeUser')
//
INSERT INTO role (role_id) VALUES ('createUser')
//
INSERT INTO role (role_id) VALUES ('deleteUser')
//
INSERT INTO role (role_id) VALUES ('deploy')
//
INSERT INTO role (role_id) VALUES ('siteRoot')
//
INSERT INTO role (role_id) VALUES ('symlink')
//
INSERT INTO role (role_id) VALUES ('separator')
//
INSERT INTO role (role_id) VALUES ('unitAdmin')
//
INSERT INTO role (role_id) VALUES ('approve')
//
INSERT INTO role (role_id) VALUES ('makeInvisible')
//
INSERT INTO role (role_id) VALUES ('openNewNavigation')
//
INSERT INTO role (role_id) VALUES ('viewWebstats')
//
INSERT INTO role (role_id) VALUES ('viewMetadata')
//
INSERT INTO role (role_id) VALUES ('pageStatusbar')
//
INSERT INTO role (role_id) VALUES ('manageHosts')
//
INSERT INTO role (role_id) VALUES ('manageSafeGuard')
//
INSERT INTO role (role_id) VALUES ('viewShowtype')
//
INSERT INTO role (role_id) VALUES ('editUrlLinkname')
//
INSERT INTO role (role_id) VALUES ('changeSearchIndexed')
//
INSERT INTO role (role_id) VALUES ('changeXmlSearchIndexed')
//
INSERT INTO role (role_id) VALUES ('updatePageLastModifiedDate')
//
-- create a site
INSERT INTO site (root_unit_id_fk, site_id, site_short, site_name) VALUES ('1', '1', 'test', 'www.test.de')
//
-- create a unit
INSERT INTO unit (unit_id, last_modified_date, name, site_id_fk) VALUES ('1', '1', 'erste rootunit', '1')
//
-- create a new user (username=admin, password=123)
INSERT INTO usr (user_id, passwd, masterRoot, active_site_id_fk, email, last_name, first_name, login_date) VALUES ('admin', 'QL0AFWMIX8NRZTKeof9cXsvbvu8=', '1', '1', 'email@adresse.de', 'Mustermann', 'Max', '0')
//
-- link the user with the unit
INSERT INTO units2users (users_id_fk, units_id_fk) VALUES ('admin','1')
//
-- link the user with the site
INSERT INTO sites2users (users_id_fk, sites_id_fk) VALUES ('admin','1')
//