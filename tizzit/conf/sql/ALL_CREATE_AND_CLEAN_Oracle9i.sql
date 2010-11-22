CREATE TABLE "COMP_ADDRESS" (
	"ADDRESS_ID" NUMBER(19) NOT NULL, 
	"ROOM_NR" VARCHAR2(255) NULL, 
	"BUILDING_LEVEL" VARCHAR2(255) NULL, 
	"BUILDING_NR" VARCHAR2(255) NULL, 
	"STREET" VARCHAR2(255) NULL, 
	"STREET_NR" VARCHAR2(255) NULL, 
	"POST_OFFICE_BOX" VARCHAR2(255) NULL, 
	"COUNTRY_CODE" VARCHAR2(3) NULL, 
	"COUNTRY" VARCHAR2(255) NULL, 
	"ZIP_CODE" VARCHAR2(50) NULL, 
	"CITY" VARCHAR2(255) NULL, 
	"PHONE1" VARCHAR2(255) NULL, 
	"PHONE2" VARCHAR2(255) NULL, 
	"FAX" VARCHAR2(255) NULL, 
	"MOBILE_PHONE" VARCHAR2(255) NULL, 
	"EMAIL" VARCHAR2(255) NULL, 
	"HOMEPAGE" VARCHAR2(255) NULL, 
	"ADDRESS_TYPE" VARCHAR2(255) DEFAULT 'Büro' NULL, 
	"MISC" CLOB NULL, 
	"PERSON_ID_FK" NUMBER(19) NULL, 
	"UNIT_ID_FK" NUMBER(10) NULL, 
	"DEPARTMENT_ID_FK" NUMBER(19) NULL, 
	"EXTERNAL_ID" VARCHAR2(255) NULL, 
	"LAST_MODIFIED_DATE" NUMBER(19) DEFAULT '0' NULL,
	PRIMARY KEY (address_id));
CREATE INDEX idx_comp_address_person_id_fk ON comp_address (person_id_fk);
CREATE INDEX idx_comp_address_unit_id_fk ON comp_address (unit_id_fk);
CREATE INDEX idx_comp_address_department_id ON comp_address (department_id_fk);
CREATE INDEX idx_comp_address_external_id ON comp_address (external_id);
CREATE INDEX idx_comp_address_last_modified ON comp_address (last_modified_date);

CREATE TABLE "COMP_DEPARTMENT" (
	"DEPARTMENT_ID" NUMBER(19) NOT NULL, 
	"NAME" VARCHAR2(255) NULL, 
	"UNIT_ID_FK" NUMBER(19) NULL,
	PRIMARY KEY (department_id));
CREATE INDEX idx_comp_department_unit_id_fk ON comp_department (unit_id_fk);

CREATE TABLE "COMP_PERSON" (
	"PERSON_ID" NUMBER(19) NOT NULL, 
	"BIRTH_DAY" VARCHAR2(50) NULL, 
	"POSITION" NUMBER(10) NULL, 
	"SALUTATION" VARCHAR2(50) NULL, 
	"SEX" NUMBER(5) NULL, 
	"TITLE" VARCHAR2(255) NULL, 
	"FIRSTNAME" VARCHAR2(255) NULL, 
	"LASTNAME" VARCHAR2(255) NULL, 
	"JOB" VARCHAR2(100) NULL, 
	"IMAGE_ID" NUMBER(10) NULL, 
	"JOB_TITLE" VARCHAR2(255) NULL, 
	"COUNTRY_JOB" VARCHAR2(100) NULL, 
	"MEDICAL_ASSOCIATION" VARCHAR2(255) NULL, 
	"LINK_MEDICAL_ASSOCIATION" VARCHAR2(255) NULL, 
	"EXTERNAL_ID" VARCHAR2(255) NULL, 
	"LAST_MODIFIED_DATE" NUMBER(19) DEFAULT '0' NULL,
	PRIMARY KEY (person_id));
CREATE INDEX idx_comp_person_image_id ON comp_person (image_id);
CREATE INDEX idx_comp_person_external_id ON comp_person (external_id);
CREATE INDEX idx_comp_person_last_modified_ ON comp_person (last_modified_date);

CREATE TABLE "COMP_TALKTIME" (
	"TALK_TIME_ID" NUMBER(19) NOT NULL, 
	"TALK_TIME_TYPE" VARCHAR2(255) NULL, 
	"TALK_TIMES" CLOB NULL, 
	"PERSON_ID_FK" NUMBER(19) NULL, 
	"UNIT_ID_FK" NUMBER(10) NULL, 
	"DEPARTMENT_ID_FK" NUMBER(19) NULL,
	PRIMARY KEY (talk_time_id));
CREATE INDEX idx_comp_talktime_person_id_fk ON comp_talktime (person_id_fk);
CREATE INDEX idx_comp_talktime_unit_id_fk ON comp_talktime (unit_id_fk);
CREATE INDEX idx_comp_talktime_department_i ON comp_talktime (department_id_fk);

CREATE TABLE "CONTENT" (
	"CONTENT_ID" NUMBER(10) NOT NULL, 
	"STATUS" NUMBER(10) NULL, 
	"TEMPLATE" VARCHAR2(100) NULL,
	"UPDATE_SEARCH_INDEX" NUMBER(5) DEFAULT '1' NULL, 
	PRIMARY KEY (content_id));

CREATE TABLE "CONTENTVERSION" (
	"CONTENT_VERSION_ID" NUMBER(10) NOT NULL, 
	"VERSION" VARCHAR2(5) NULL, 
	"HEADING" VARCHAR2(255) NULL, 
	"TEXT" CLOB NULL, 
	"CREATE_DATE" NUMBER(19) NULL, 
	"CREATOR" VARCHAR2(255) NULL, 
	"CONTENT_ID_FK" NUMBER(10) NULL, 
	"LOCK_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (content_version_id));
CREATE INDEX idx_contentversion_version ON contentversion (version);
CREATE INDEX idx_contentversion_create_date ON contentversion (create_date);
CREATE INDEX idx_contentversion_creator ON contentversion (creator);
CREATE INDEX idx_contentversion_content_id_ ON contentversion (content_id_fk);
CREATE INDEX idx_contentversion_lock_id_fk ON contentversion (lock_id_fk);

CREATE TABLE "DEPARTMENTS2PERSONS" (
	"PERSONS_ID_FK" NUMBER(19) NOT NULL, 
	"DEPARTMENTS_ID_FK" NUMBER(19) NOT NULL,
	PRIMARY KEY (persons_id_fk, departments_id_fk));

CREATE TABLE "DOCUMENT" (
	"DOCUMENT_ID" NUMBER(10) NOT NULL, 
	"DOCUMENT" BLOB NULL, 
	"DOCUMENT_NAME" VARCHAR2(255) NULL, 
	"TIME_STAMP" NUMBER(19) NULL, 
	"MIME_TYPE" VARCHAR2(50) NULL, 
	"UNIT_ID_FK" NUMBER(10) NULL, 
	"USE_COUNT_LAST_VERSION" NUMBER(10) NULL, 
	"USE_COUNT_PUBLISH_VERSION" NUMBER(10) NULL,
	"UPDATE_SEARCH_INDEX" NUMBER(5) DEFAULT '1' NULL, 
	"VIEW_COMPONENT_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (document_id));
CREATE INDEX idx_document_unit_id_fk ON document (unit_id_fk);
/* change the identifier cause it was too long */
CREATE INDEX idx_document_view_comp_id_fk ON document (view_component_id_fk);

CREATE TABLE "EDITION" (
	"EDITION_ID" NUMBER(10) NOT NULL, 
	"CREATOR_ID_FK" VARCHAR2(255) NULL, 
	"USER_COMMENT" CLOB NULL, 
	"CREATION_DATE" NUMBER(19) NULL, 
	"STATUS" NUMBER(5) NULL, 
	"UNIT_ID" NUMBER(10) NULL, 
	"VIEW_DOCUMENT_ID" NUMBER(10) NULL,
	"WORK_SERVER_EDITION_ID" NUMBER(10) NULL,
	"DEPLOY_STATUS" BLOB NULL,
	"START_ACTION_TIMESTAMP" NUMBER(19) NULL,
	"END_ACTION_TIMESTAMP" NUMBER(19) NULL,
	"EDITION_FILE_NAME" VARCHAR2(255) NULL,
	"NEEDS_IMPORT" NUMBER(5) DEFAULT '0' NULL,
	"NEEDS_DEPLOY" NUMBER(5) DEFAULT '0' NULL,
	"USE_NEW_IDS" NUMBER(5) DEFAULT '0' NULL,
	"SITE_ID" NUMBER(10) NULL,
	"VIEW_COMPONENT_ID" NUMBER(10) NULL,
	"DEPLOY_TYPE" NUMBER(10) NULL,
	PRIMARY KEY (edition_id));
CREATE INDEX idx_edition_creator_id_fk ON edition (creator_id_fk);
CREATE INDEX idx_edition_unit_id ON edition (unit_id);
CREATE INDEX idx_edition_view_document_id ON edition (view_document_id);

CREATE TABLE "GROUPS2ROLES" (
	"ROLES_ID_FK" VARCHAR2(255) NOT NULL, 
	"GROUPS_ID_FK" NUMBER(10) NOT NULL,
	PRIMARY KEY (roles_id_fk, groups_id_fk));

CREATE TABLE "GROUPS2USERS" (
	"USERS_ID_FK" VARCHAR2(255) NOT NULL, 
	"GROUPS_ID_FK" NUMBER(10) NOT NULL,
	PRIMARY KEY (users_id_fk, groups_id_fk));

CREATE TABLE "HOST" (
	"HOST_NAME" VARCHAR2(255) NOT NULL, 
	"LIVESERVER" NUMBER(5) DEFAULT '0' NULL,
	"SITE_ID_FK" NUMBER(10) NULL, 
	"STARTPAGE_VC_ID_FK" NUMBER(10) NULL, 
	"UNIT_ID_FK" NUMBER(10) NULL, 
	"REDIRECT_URL" VARCHAR2(255) NULL, 
	"REDIRECT_HOST_NAME_ID_FK" VARCHAR2(255) NULL,
	PRIMARY KEY (host_name));
CREATE INDEX idx_host_site_id_fk ON host (site_id_fk);
CREATE INDEX idx_host_startpage_vc_id_fk ON host (startpage_vc_id_fk);
CREATE INDEX idx_host_unit_id_fk ON host (unit_id_fk);
CREATE INDEX idx_host_redirect_host_name_id ON host (redirect_host_name_id_fk);

CREATE TABLE "KEYGEN" (
	"IDX" NUMBER(10) NOT NULL, 
	"NAME" VARCHAR2(100) NOT NULL,
	PRIMARY KEY (name));

CREATE TABLE "LOCKS" (
	"LOCK_ID" NUMBER(10) NOT NULL, 
	"CREATE_DATE" NUMBER(19) NULL, 
	"OWNER_ID_FK" VARCHAR2(255) NULL,
	PRIMARY KEY (lock_id));
CREATE INDEX idx_locks_owner_id_fk ON locks (owner_id_fk);

CREATE TABLE "PERSONS2UNITS" (
	"PERSONS_ID_FK" NUMBER(19) NOT NULL, 
	"UNITS_ID_FK" NUMBER(10) NOT NULL,
	PRIMARY KEY (persons_id_fk, units_id_fk));

CREATE TABLE "PICTURE" (
	"PICTURE_ID" NUMBER(10) NOT NULL, 
	"THUMBNAIL" BLOB NULL, 
	"PICTURE" BLOB NULL, 
	"PREVIEW" BLOB NULL, 
	"TIME_STAMP" NUMBER(19) NULL, 
	"MIME_TYPE" VARCHAR2(50) NULL, 
	"UNIT_ID_FK" NUMBER(10) NULL, 
	"ALT_TEXT" VARCHAR2(255) NULL, 
	"PICTURE_NAME" VARCHAR2(255) NULL, 
	"HEIGHT" NUMBER(10) NULL, 
	"WIDTH" NUMBER(10) NULL,
	"THUMBNAIL_POPUP" NUMBER(5) DEFAULT '0' NULL,
	"TITLE" VARCHAR(255) NULL,
	"VIEW_COMPONENT_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (picture_id));
CREATE INDEX idx_picture_unit_id_fk ON picture (unit_id_fk);
CREATE INDEX idx_picture_view_comp_id_fk ON picture (view_component_id_fk);

CREATE TABLE "ROLE" (
	"ROLE_ID" VARCHAR2(255) NOT NULL,
	PRIMARY KEY (role_id));

CREATE TABLE "SGROUP" (
	"GROUP_ID" NUMBER(10) NOT NULL, 
	"GROUP_NAME" VARCHAR2(255) NULL, 
	"SITE_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (group_id));
CREATE INDEX idx_sgroup_site_id_fk ON sgroup (site_id_fk);

CREATE TABLE "SITE" (
	"SITE_ID" NUMBER(10) NOT NULL, 
	"SITE_NAME" VARCHAR2(255) NULL, 
	"SITE_SHORT" VARCHAR2(50) NULL, 
	"SITE_CONFIG" CLOB NULL, 
	"MANDATOR_DIR" VARCHAR2(255) NULL, 
	"ROOT_UNIT_ID_FK" NUMBER(10) NULL, 
	"DEFAULT_VIEW_DOCUMENT_ID_FK" NUMBER(10) NULL, 
	"CACHE_EXPIRE" NUMBER(19) NULL, 
	"SITE_GROUP_ID_FK" NUMBER(10) NULL, 
	"WYSIWYG_IMAGE_URL" VARCHAR2(255) NULL, 
	"HELP_URL" VARCHAR2(255) NULL, 
	"DCF_URL" VARCHAR2(255) NULL, 
	"PAGE_NAME_FULL" VARCHAR2(255) NULL, 
	"PAGE_NAME_CONTENT" VARCHAR2(255) NULL, 
	"PAGE_NAME_SEARCH" VARCHAR2(255) NULL, 
	"LAST_MODIFIED_DATE" NUMBER(19) DEFAULT '0' NULL,
	"UPDATE_SITE_INDEX" NUMBER(5) NULL, 
	"EXTERNAL_SITE_SEARCH" NUMBER(5) NULL,	
	"PREVIEW_URL_LIVE_SERVER" VARCHAR(255) null,
	"PREVIEW_URL_WORK_SERVER" VARCHAR(255) null,
	PRIMARY KEY (site_id));
CREATE INDEX idx_site_site_short ON site (site_short);
CREATE INDEX idx_site_mandator_dir ON site (mandator_dir);
CREATE INDEX idx_site_last_modified_date ON site (last_modified_date);

CREATE TABLE "SITE_GROUP" (
	"SITE_GROUP_ID" NUMBER(10) NOT NULL, 
	"SITE_GROUP_NAME" VARCHAR2(255) NULL,
	PRIMARY KEY (site_group_id));

CREATE TABLE "SITES2USERS" (
	"USERS_ID_FK" VARCHAR2(255) NOT NULL, 
	"SITES_ID_FK" NUMBER(10) NOT NULL,
	PRIMARY KEY (users_id_fk, sites_id_fk));

CREATE TABLE "TASK" (
	"TASK_ID" NUMBER(10) NOT NULL, 
	"TASK_TYPE" NUMBER(5) NULL, 
	"RECEIVER_ROLE" VARCHAR2(50) NULL, 
	"UNIT_ID_FK" NUMBER(10) NULL, 
	"RECEIVER" VARCHAR2(255) NULL, 
	"SENDER" VARCHAR2(255) NULL, 
	"USER_COMMENT" CLOB NULL, 
	"STATUS" NUMBER(5) NULL, 
	"CREATION_DATE" NUMBER(19) NULL,
	PRIMARY KEY (task_id));
CREATE INDEX idx_task_receiver_role ON task (receiver_role);
CREATE INDEX idx_task_unit_id_fk ON task (unit_id_fk);
CREATE INDEX idx_task_receiver ON task (receiver);
CREATE INDEX idx_task_sender ON task (sender);

CREATE TABLE "TASK2VIEW_COMPONENTS" (
	"TASK_ID_FK" NUMBER(10) NOT NULL, 
	"VIEW_COMPONENTS_ID_FK" NUMBER(10) NOT NULL,
	PRIMARY KEY (task_id_fk, view_components_id_fk));

CREATE TABLE "UNIT" (
	"UNIT_ID" NUMBER(10) NOT NULL, 
	"NAME" VARCHAR2(255) NULL, 
	"IMAGE_ID" NUMBER(10) NULL, 
	"LOGO_ID" NUMBER(10) NULL, 
	"SITE_ID_FK" NUMBER(10) NULL, 
	"LAST_MODIFIED_DATE" NUMBER(19) DEFAULT '0' NULL,
	PRIMARY KEY (unit_id));
CREATE INDEX idx_unit_image_id ON unit (image_id);
CREATE INDEX idx_unit_logo_id ON unit (logo_id);
CREATE INDEX idx_unit_site_id_fk ON unit (site_id_fk);
CREATE INDEX idx_unit_last_modified_date ON unit (last_modified_date);

CREATE TABLE "UNITS2USERS" (
	"USERS_ID_FK" VARCHAR2(255) NOT NULL, 
	"UNITS_ID_FK" NUMBER(10) NOT NULL,
	PRIMARY KEY (users_id_fk, units_id_fk));

CREATE TABLE "USR" (
	"USER_ID" VARCHAR2(255) NOT NULL, 
	"FIRST_NAME" VARCHAR2(255) NULL, 
	"LAST_NAME" VARCHAR2(255) NULL, 
	"EMAIL" VARCHAR2(255) NULL, 
	"CONFIG_XML" CLOB NULL, 
	"LOGIN_DATE" NUMBER(19) NULL, 
	"ACTIVE_SITE_ID_FK" NUMBER(10) NULL, 
	"MASTERROOT" NUMBER(5) DEFAULT '0' NULL, 
	"PASSWD" VARCHAR2(60) NULL,
	PRIMARY KEY (user_id));
CREATE INDEX idx_usr_active_site_id_fk ON usr (active_site_id_fk);

CREATE TABLE "VIEWCOMPONENT" (
	"VIEW_COMPONENT_ID" NUMBER(10) NOT NULL, 
	"PARENT_ID_FK" NUMBER(10) NULL, 
	"FIRST_CHILD_ID_FK" NUMBER(10) NULL, 
	"PREV_NODE_ID_FK" NUMBER(10) NULL, 
	"NEXT_NODE_ID_FK" NUMBER(10) NULL, 
	"UNIT_ID_FK" NUMBER(10) NULL, 
	"STATUS" NUMBER(10) NULL, 
	"SHOW_TYPE" NUMBER(5) NULL, 
	"VIEW_TYPE" NUMBER(5) NULL, 
	"VISIBLE" NUMBER(5) NULL, 
	"SEARCH_INDEXED" NUMBER(5) NULL, 
	"XML_SEARCH_INDEXED" NUMBER(5) DEFAULT '1' NULL, 
	"DISPLAY_LINK_NAME" VARCHAR2(255) NULL, 
	"LINK_DESCRIPTION" VARCHAR2(255) NULL, 
	"URL_LINK_NAME" VARCHAR2(255) NULL, 
	"APPROVED_LINK_NAME" VARCHAR2(255) NULL, 
	"VIEW_LEVEL" VARCHAR2(10) NULL, 
	"VIEW_INDEX" VARCHAR2(10) NULL, 
	"ONLINE_START" NUMBER(19) NULL, 
	"ONLINE_STOP" NUMBER(19) NULL, 
	"REFERENCE" VARCHAR2(255) NULL, 
	"VIEW_DOCUMENT_ID_FK" NUMBER(10) NULL, 
	"ONLINE_STATE" NUMBER(5) DEFAULT '0' NULL, 
	"DEPLOY_COMMAND" NUMBER(5) DEFAULT '0' NULL, 
	"META_DATA" CLOB NULL, 
	"META_DESCRIPTION" CLOB NULL, 
	"CREATE_DATE" NUMBER(19) DEFAULT '0' NULL, 
	"LAST_MODIFIED_DATE" NUMBER(19) DEFAULT '0' NULL, 
	"REALM2VC_ID_FK" NUMBER(10) NULL, 
	"DISPLAY_SETTINGS" NUMBER(5) DEFAULT '0' NULL, 
	"USER_LAST_MODIFIED_DATE" NUMBER(19) DEFAULT '0' NULL,
	PRIMARY KEY (view_component_id));
CREATE INDEX idx_viewcomponent_parent_id_fk ON viewcomponent (parent_id_fk);
CREATE INDEX idx_viewcomponent_first_child_ ON viewcomponent (first_child_id_fk);
CREATE INDEX idx_viewcomponent_prev_node_id ON viewcomponent (prev_node_id_fk);
CREATE INDEX idx_viewcomponent_next_node_id ON viewcomponent (next_node_id_fk);
CREATE INDEX idx_viewcomponent_unit_id_fk ON viewcomponent (unit_id_fk);
CREATE INDEX idx_viewcomponent_display_link ON viewcomponent (display_link_name);
CREATE INDEX idx_viewcomponent_reference ON viewcomponent (reference);
CREATE INDEX idx_viewcomponent_view_documen ON viewcomponent (view_document_id_fk);
CREATE INDEX idx_viewcomponent_realm2vc_id_ ON viewcomponent (realm2vc_id_fk);

CREATE TABLE "VIEWDOCUMENT" (
	"VIEW_DOCUMENT_ID" NUMBER(10) NOT NULL, 
	"LANGUAGE" VARCHAR2(50) NULL, 
	"VIEW_TYPE" VARCHAR2(50) NULL, 
	"VIEW_COMPONENT_ID_FK" NUMBER(10) NULL, 
	"SITE_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (view_document_id));
CREATE INDEX idx_viewdocument_language ON viewdocument (language);
CREATE INDEX idx_viewdocument_view_type ON viewdocument (view_type);
CREATE INDEX idx_viewdocument_view_componen ON viewdocument (view_component_id_fk);
CREATE INDEX idx_viewdocument_site_id_fk ON viewdocument (site_id_fk);

CREATE TABLE "REALM_LDAP" (
	"LDAP_REALM_ID" NUMBER(10) NOT NULL, 
	"REALM_NAME" VARCHAR2(255) NULL, 
	"LDAP_URL" VARCHAR2(255) NULL, 
	"LDAP_AUTHENTICATION_TYPE" VARCHAR2(255) NULL, 
	"LDAP_SUFFIX" VARCHAR2(255) NULL, 
	"LDAP_PREFIX" VARCHAR2(255) NULL, 
	"LOGIN_PAGE_ID" VARCHAR2(255) NULL, 
	"SITE_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (ldap_realm_id));

CREATE TABLE "REALM_SIMPLE_PW" (
	"SIMPLE_PW_REALM_ID" NUMBER(10) NOT NULL, 
	"REALM_NAME" VARCHAR2(255) NULL, 
	"LOGIN_PAGE_ID" VARCHAR2(255) NULL, 
	"OWNER_ID_FK" VARCHAR2(255) NULL, 
	"SITE_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (simple_pw_realm_id));

CREATE TABLE "REALM_SIMPLE_PW_USER" (
	"SIMPLE_PW_REALM_USER_ID" NUMBER(10) NOT NULL, 
	"USER_NAME" VARCHAR2(255) NULL, 
	"PASSWORD" VARCHAR2(255) NULL, 
	"ROLES" VARCHAR2(1024) NULL, 
	"SIMPLE_PW_REALM_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (simple_pw_realm_user_id));

CREATE TABLE "REALM_JDBC" (
	"JDBC_REALM_ID" NUMBER(10) NOT NULL, 
	"STATEMENT_USER" VARCHAR2(255) NULL, 
	"STATEMENT_ROLE_PER_USER" VARCHAR2(255) NULL, 
	"JNDI_NAME" VARCHAR2(255) NULL, 
	"REALM_NAME" VARCHAR2(255) NULL, 
	"LOGIN_PAGE_ID" VARCHAR2(255) NULL, 
	"SITE_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (jdbc_realm_id));

CREATE TABLE "REALM_JAAS" (
	"JAAS_REALM_ID" NUMBER(10) NOT NULL, 
	"REALM_NAME" VARCHAR2(255) NULL, 
	"LOGIN_PAGE_ID" VARCHAR2(255) NULL, 
	"JAAS_POLICY_NAME" VARCHAR2(255) NULL, 
	"SITE_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (jaas_realm_id));

CREATE TABLE "REALM2VIEW_COMPONENT" (
	"REALM2VIEW_COMPONENT_ID" NUMBER(10) NOT NULL, 
	"ROLE_NEEDED" VARCHAR2(255) NULL, 
	"SIMPLE_PW_REALM_ID_FK" NUMBER(10) NULL, 
	"JDBC_REALM_ID_FK" NUMBER(10) NULL, 
	"LDAP_REALM_ID_FK" NUMBER(10) NULL, 
	"JAAS_REALM_ID_FK" NUMBER(10) NULL, 
	"VIEW_COMPONENT_ID_FK" NUMBER(10) NULL, 
	"LOGIN_PAGE_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (realm2view_component_id));

CREATE TABLE "SHORT_LINK" (
	"SHORT_LINK_ID" NUMBER(10) NOT NULL, 
	"SHORT_LINK" VARCHAR2(255) NULL, 
	"REDIRECT_URL" VARCHAR2(255) NULL, 
	"SITE_ID_FK" NUMBER(10) NULL, 
	"VIEW_DOCUMENT_ID_FK" NUMBER(10) NULL,
	PRIMARY KEY (short_link_id));
CREATE INDEX idx_short_link_site_id_fk ON short_link (site_id_fk);
CREATE INDEX idx_short_link_view_document_i ON short_link (view_document_id_fk);

CREATE TABLE "ACCESSROLE" (
	"ROLE_ID" VARCHAR2(255) NOT NULL,
	PRIMARY KEY (role_id));
	
CREATE TABLE "ROLES2VIEW_COMPONENTS" (
	"ROLES_ID_FK" VARCHAR2(255) NOT NULL, 
	"VIEW_COMPONENTS_ID_FK" NUMBER(10) NOT NULL,
	PRIMARY KEY (roles_id_fk, view_components_id_fk));

INSERT INTO keygen (idx, name) VALUES ('1', 'address.address_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'content.content_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'contentversion.content_version_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'department.department_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'document.document_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'edition.edition_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'editionslice.edition_slice_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'locks.lock_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'person.person_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'picture.picture_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'talktime.talk_time_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'task.task_id');

INSERT INTO keygen (idx, name) VALUES ('2', 'unit.unit_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'viewcomponent.view_component_id');

INSERT INTO keygen (idx, name) VALUES ('1', 'viewdocument.view_document_id');

INSERT INTO keygen (idx, name) VALUES ('2', 'site.site_id');

INSERT INTO role (role_id) VALUES ('changeUser');

INSERT INTO role (role_id) VALUES ('createUser');

INSERT INTO role (role_id) VALUES ('deleteUser');

INSERT INTO role (role_id) VALUES ('deploy');

INSERT INTO role (role_id) VALUES ('siteRoot');

INSERT INTO role (role_id) VALUES ('symlink');

INSERT INTO role (role_id) VALUES ('separator');

INSERT INTO role (role_id) VALUES ('unitAdmin');

INSERT INTO role (role_id) VALUES ('approve');

INSERT INTO role (role_id) VALUES ('makeInvisible');

INSERT INTO role (role_id) VALUES ('openNewNavigation');

INSERT INTO role (role_id) VALUES ('viewWebstats');

INSERT INTO role (role_id) VALUES ('viewMetadata');

INSERT INTO role (role_id) VALUES ('pageStatusbar');

INSERT INTO role (role_id) VALUES ('manageHosts');

INSERT INTO role (role_id) VALUES ('manageSafeGuard');

INSERT INTO role (role_id) VALUES ('viewShowtype');

INSERT INTO role (role_id) VALUES ('editUrlLinkname');

INSERT INTO role (role_id) VALUES ('changeSearchIndexed');

INSERT INTO role (role_id) VALUES ('changeXmlSearchIndexed');

INSERT INTO role (role_id) VALUES ('updatePageLastModifiedDate');

-- create a site
INSERT INTO site (root_unit_id_fk, site_id, site_short, site_name, mandator_dir) VALUES ('1', '1', 'test', 'www.test.de', '/mandatordir/dcf');

-- create a unit
INSERT INTO unit (unit_id, last_modified_date, name, site_id_fk) VALUES ('1', '1', 'erste rootunit', '1');

-- create a new user (username=admin, password=123)
INSERT INTO usr (user_id, passwd, masterRoot, active_site_id_fk, email, last_name, first_name, login_date) VALUES ('admin', 'QL0AFWMIX8NRZTKeof9cXsvbvu8=', '1', '1', 'email@adresse.de', 'Mustermann', 'Max', '0');

-- link the user with the unit
INSERT INTO units2users (users_id_fk, units_id_fk) VALUES ('admin','1');

-- link the user with the site
INSERT INTO sites2users (users_id_fk, sites_id_fk) VALUES ('admin','1');