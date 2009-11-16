CREATE TABLE COMP_ADDRESS (
	address_id BIGINT NOT NULL, 
	room_nr VARCHAR(255) NULL, 
	building_level VARCHAR(255) NULL, 
	building_nr VARCHAR(255) NULL, 
	street VARCHAR(255) NULL, 
	street_nr VARCHAR(255) NULL, 
	post_office_box VARCHAR(255) NULL, 
	country_code VARCHAR(3) NULL, 
	country VARCHAR(255) NULL, 
	zip_code VARCHAR(50) NULL, 
	city VARCHAR(255) NULL, 
	phone1 VARCHAR(255) NULL, 
	phone2 VARCHAR(255) NULL, 
	fax VARCHAR(255) NULL, 
	mobile_phone VARCHAR(255) NULL, 
	email VARCHAR(255) NULL, 
	homepage VARCHAR(255) NULL, 
	address_type VARCHAR(255) NULL DEFAULT 'Büro', 
	misc ntext NULL, 
	person_id_fk BIGINT NULL, 
	unit_id_fk INTEGER NULL, 
	department_id_fk BIGINT NULL, 
	external_id VARCHAR(255) NULL, 
	last_modified_date BIGINT NULL DEFAULT '0', 
	PRIMARY KEY CLUSTERED(address_id)
);
CREATE INDEX idx_comp_address_person_id_fk ON comp_address (person_id_fk);
CREATE INDEX idx_comp_address_unit_id_fk ON comp_address (unit_id_fk);
CREATE INDEX idx_comp_address_department_id_fk ON comp_address (department_id_fk);
CREATE INDEX idx_comp_address_external_id ON comp_address (external_id);
CREATE INDEX idx_comp_address_last_modified_date ON comp_address (last_modified_date);

CREATE TABLE COMP_DEPARTMENT (
	department_id BIGINT NOT NULL, 
	name VARCHAR(255) NULL, 
	unit_id_fk BIGINT NULL, 
	PRIMARY KEY CLUSTERED(department_id)
);
CREATE INDEX idx_comp_department_unit_id_fk ON comp_department (unit_id_fk);

CREATE TABLE COMP_PERSON (
	person_id BIGINT NOT NULL, 
	birth_day VARCHAR(50) NULL, 
	position INTEGER NULL, 
	salutation VARCHAR(50) NULL, 
	sex SMALLINT NULL, 
	title VARCHAR(255) NULL, 
	firstname VARCHAR(255) NULL, 
	lastname VARCHAR(255) NULL, 
	job VARCHAR(100) NULL, 
	image_id INTEGER NULL, 
	job_title VARCHAR(255) NULL, 
	country_job VARCHAR(100) NULL, 
	medical_association VARCHAR(255) NULL, 
	link_medical_association VARCHAR(255) NULL, 
	external_id VARCHAR(255) NULL, 
	last_modified_date BIGINT NULL DEFAULT '0', 
	PRIMARY KEY CLUSTERED(person_id)
);
CREATE INDEX idx_comp_person_image_id ON comp_person (image_id);
CREATE INDEX idx_comp_person_external_id ON comp_person (external_id);
CREATE INDEX idx_comp_person_last_modified_date ON comp_person (last_modified_date);

CREATE TABLE COMP_PERSONTOUNITLINK (
	personToUnitLink_id INTEGER NOT NULL, 
	role_type INTEGER NULL, 
	unit_id_fk INTEGER NULL, 
	person_id_fk BIGINT NULL, 
	PRIMARY KEY CLUSTERED(personToUnitLink_id)
);
CREATE INDEX idx_comp_persontounitlink_unit_id_fk ON comp_persontounitlink (unit_id_fk);
CREATE INDEX idx_comp_persontounitlink_person_id_fk ON comp_persontounitlink (person_id_fk);

CREATE TABLE COMP_TALKTIME (
	talk_time_id BIGINT NOT NULL, 
	talk_time_type VARCHAR(255) NULL, 
	talk_times ntext NULL, 
	person_id_fk BIGINT NULL, 
	unit_id_fk INTEGER NULL, 
	department_id_fk BIGINT NULL, 
	PRIMARY KEY CLUSTERED(talk_time_id)
);
CREATE INDEX idx_comp_talktime_person_id_fk ON comp_talktime (person_id_fk);
CREATE INDEX idx_comp_talktime_unit_id_fk ON comp_talktime (unit_id_fk);
CREATE INDEX idx_comp_talktime_department_id_fk ON comp_talktime (department_id_fk);

CREATE TABLE CONTENT (
	content_id INTEGER NOT NULL, 
	status INTEGER NULL, 
	template VARCHAR(100) NULL,
	UPDATE_SEARCH_INDEX SMALLINT NULL DEFAULT '1', 
	PRIMARY KEY CLUSTERED(content_id)
);

CREATE TABLE CONTENTVERSION (
	content_version_id INTEGER NOT NULL, 
	version VARCHAR(5) NULL, 
	heading VARCHAR(255) NULL, 
	text ntext NULL, 
	version_comment ntext NULL, 
	create_date BIGINT NULL, 
	creator VARCHAR(255) NULL, 
	content_id_fk INTEGER NULL, 
	lock_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(content_version_id)
);
CREATE INDEX idx_contentversion_version ON contentversion (version);
CREATE INDEX idx_contentversion_create_date ON contentversion (create_date);
CREATE INDEX idx_contentversion_creator ON contentversion (creator);
CREATE INDEX idx_contentversion_content_id_fk ON contentversion (content_id_fk);
CREATE INDEX idx_contentversion_lock_id_fk ON contentversion (lock_id_fk);

CREATE TABLE DEPARTMENTS2PERSONS (
	persons_id_fk BIGINT NOT NULL, 
	departments_id_fk BIGINT NOT NULL, 
	PRIMARY KEY CLUSTERED(persons_id_fk, departments_id_fk)
);

CREATE TABLE DOCUMENT (
	document_id INTEGER NOT NULL, 
	document IMAGE NULL, 
	document_name VARCHAR(255) NULL, 
	time_stamp BIGINT NULL, 
	mime_type VARCHAR(50) NULL, 
	unit_id_fk INTEGER NULL, 
	use_count_last_version INTEGER NULL, 
	use_count_publish_version INTEGER NULL, 
	UPDATE_SEARCH_INDEX SMALLINT NULL DEFAULT '1', 
	PRIMARY KEY CLUSTERED(document_id)
);
CREATE INDEX idx_document_unit_id_fk ON document (unit_id_fk);

CREATE TABLE EDITION (
	edition_id INTEGER NOT NULL, 
	creator_id_fk VARCHAR(255) NULL, 
	user_comment ntext NULL, 
	creation_date BIGINT NULL, 
	status SMALLINT NULL, 
	unit_id INTEGER NULL, 
	view_document_id INTEGER NULL,
	WORK_SERVER_EDITION_ID INTEGER NULL,
	DEPLOY_STATUS IMAGE NULL,
	START_ACTION_TIMESTAMP BIGINT NULL,
	END_ACTION_TIMESTAMP BIGINT NULL,	
	PRIMARY KEY CLUSTERED(edition_id)
);
CREATE INDEX idx_edition_creator_id_fk ON edition (creator_id_fk);
CREATE INDEX idx_edition_unit_id ON edition (unit_id);
CREATE INDEX idx_edition_view_document_id ON edition (view_document_id);

CREATE TABLE EDITIONSLICE (
	edition_slice_id INTEGER NOT NULL, 
	edition_id INTEGER NOT NULL, 
	slice_nr INTEGER NOT NULL, 
	slice_data IMAGE NULL, 
	PRIMARY KEY CLUSTERED(edition_slice_id)
);
CREATE INDEX idx_editionslice_edition_id ON editionslice (edition_id);
CREATE INDEX idx_editionslice_slice_nr ON editionslice (slice_nr);

CREATE TABLE GROUPS2ROLES (
	roles_id_fk VARCHAR(255) NOT NULL, 
	groups_id_fk INTEGER NOT NULL, 
	PRIMARY KEY CLUSTERED(roles_id_fk, groups_id_fk)
);

CREATE TABLE GROUPS2USERS (
	users_id_fk VARCHAR(255) NOT NULL, 
	groups_id_fk INTEGER NOT NULL, 
	PRIMARY KEY CLUSTERED(users_id_fk, groups_id_fk)
);

CREATE TABLE HOST (
	host_name VARCHAR(255) NOT NULL, 
	liveserver SMALLINT DEFAULT '0' NULL,
	site_id_fk INTEGER NULL, 
	startpage_vc_id_fk INTEGER NULL, 
	unit_id_fk INTEGER NULL, 
	redirect_url VARCHAR(255) NULL, 
	redirect_host_name_id_fk VARCHAR(255) NULL, 
	PRIMARY KEY CLUSTERED(host_name)
);
CREATE INDEX idx_host_site_id_fk ON host (site_id_fk);
CREATE INDEX idx_host_startpage_vc_id_fk ON host (startpage_vc_id_fk);
CREATE INDEX idx_host_unit_id_fk ON host (unit_id_fk);
CREATE INDEX idx_host_redirect_host_name_id_fk ON host (redirect_host_name_id_fk);

CREATE TABLE KEYGEN (
	idx INTEGER NOT NULL, 
	name VARCHAR(100) NOT NULL, 
	PRIMARY KEY CLUSTERED(name)
);

CREATE TABLE LOCKS (
	lock_id INTEGER NOT NULL, 
	create_date BIGINT NULL, 
	owner_id_fk VARCHAR(255) NULL, 
	PRIMARY KEY CLUSTERED(lock_id)
);
CREATE INDEX idx_locks_owner_id_fk ON locks (owner_id_fk);

CREATE TABLE PERSONS2UNITS (
	persons_id_fk BIGINT NOT NULL, 
	units_id_fk INTEGER NOT NULL, 
	PRIMARY KEY CLUSTERED(persons_id_fk, units_id_fk)
);

CREATE TABLE PICTURE (
	picture_id INTEGER NOT NULL, 
	thumbnail IMAGE NULL, 
	picture IMAGE NULL, 
	preview IMAGE NULL, 
	time_stamp BIGINT NULL, 
	mime_type VARCHAR(50) NULL, 
	unit_id_fk INTEGER NULL, 
	alt_text VARCHAR(255) NULL, 
	picture_name VARCHAR(255) NULL, 
	height INTEGER NULL, 
	width INTEGER NULL, 
	PRIMARY KEY CLUSTERED(picture_id)
);
CREATE INDEX idx_picture_unit_id_fk ON picture (unit_id_fk);

CREATE TABLE ROLE (
	role_id VARCHAR(255) NOT NULL, 
	PRIMARY KEY CLUSTERED(role_id)
);

CREATE TABLE SGROUP (
	group_id INTEGER NOT NULL, 
	group_name VARCHAR(255) NULL, 
	site_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(group_id)
);
CREATE INDEX idx_sgroup_site_id_fk ON sgroup (site_id_fk);

CREATE TABLE SITE (
	site_id INTEGER NOT NULL, 
	site_name VARCHAR(255) NULL, 
	site_short VARCHAR(50) NULL, 
	site_config ntext NULL, 
	mandator_dir VARCHAR(255) NULL, 
	root_unit_id_fk INTEGER NULL, 
	default_view_document_id_fk INTEGER NULL, 
	cache_expire BIGINT NULL, 
	site_group_id_fk INTEGER NULL, 
	wysiwyg_image_url VARCHAR(255) NULL, 
	help_url VARCHAR(255) NULL, 
	dcf_url VARCHAR(255) NULL, 
	preview_url VARCHAR(255) NULL, 
	page_name_full VARCHAR(255) NULL, 
	page_name_content VARCHAR(255) NULL, 
	page_name_search VARCHAR(255) NULL, 
	last_modified_date BIGINT NULL DEFAULT '0', 
	PRIMARY KEY CLUSTERED(site_id)
);
CREATE INDEX idx_site_site_short ON site (site_short);
CREATE INDEX idx_site_mandator_dir ON site (mandator_dir);
CREATE INDEX idx_site_last_modified_date ON site (last_modified_date);

CREATE TABLE SITE_GROUP (
	site_group_id INTEGER NOT NULL, 
	site_group_name VARCHAR(255) NULL, 
	PRIMARY KEY CLUSTERED(site_group_id)
);

CREATE TABLE SITES2USERS (
	users_id_fk VARCHAR(255) NOT NULL, 
	sites_id_fk INTEGER NOT NULL, 
	PRIMARY KEY CLUSTERED(users_id_fk, sites_id_fk)
);

CREATE TABLE TASK (
	task_id INTEGER NOT NULL, 
	task_type SMALLINT NULL, 
	receiver_role VARCHAR(50) NULL, 
	unit_id_fk INTEGER NULL, 
	receiver VARCHAR(255) NULL, 
	sender VARCHAR(255) NULL, 
	user_comment ntext NULL, 
	status SMALLINT NULL, 
	creation_date BIGINT NULL, 
	PRIMARY KEY CLUSTERED(task_id)
);
CREATE INDEX idx_task_receiver_role ON task (receiver_role);
CREATE INDEX idx_task_unit_id_fk ON task (unit_id_fk);
CREATE INDEX idx_task_receiver ON task (receiver);
CREATE INDEX idx_task_sender ON task (sender);

CREATE TABLE TASK2VIEW_COMPONENTS (
	task_id_fk INTEGER NOT NULL, 
	view_components_id_fk INTEGER NOT NULL, 
	PRIMARY KEY CLUSTERED(task_id_fk, view_components_id_fk)
);

CREATE TABLE UNIT (
	unit_id INTEGER NOT NULL, 
	name VARCHAR(255) NULL, 
	image_id INTEGER NULL, 
	logo_id INTEGER NULL, 
	site_id_fk INTEGER NULL, 
	last_modified_date BIGINT NULL DEFAULT '0', 
	PRIMARY KEY CLUSTERED(unit_id)
);
CREATE INDEX idx_unit_image_id ON unit (image_id);
CREATE INDEX idx_unit_logo_id ON unit (logo_id);
CREATE INDEX idx_unit_site_id_fk ON unit (site_id_fk);
CREATE INDEX idx_unit_last_modified_date ON unit (last_modified_date);

CREATE TABLE UNITS2USERS (
	users_id_fk VARCHAR(255) NOT NULL, 
	units_id_fk INTEGER NOT NULL, 
	PRIMARY KEY CLUSTERED(users_id_fk, units_id_fk)
);

CREATE TABLE USR (
	user_id VARCHAR(255) NOT NULL, 
	first_name VARCHAR(255) NULL, 
	last_name VARCHAR(255) NULL, 
	email VARCHAR(255) NULL, 
	config_xml ntext NULL, 
	login_date BIGINT NULL, 
	active_site_id_fk INTEGER NULL, 
	masterRoot SMALLINT NULL DEFAULT '0', 
	passwd VARCHAR(60) NULL, 
	PRIMARY KEY CLUSTERED(user_id)
);
CREATE INDEX idx_usr_active_site_id_fk ON usr (active_site_id_fk);

CREATE TABLE VIEWCOMPONENT (
	view_component_id INTEGER NOT NULL, 
	parent_id_fk INTEGER NULL, 
	first_child_id_fk INTEGER NULL, 
	prev_node_id_fk INTEGER NULL, 
	next_node_id_fk INTEGER NULL, 
	unit_id_fk INTEGER NULL, 
	status INTEGER NULL, 
	show_type SMALLINT NULL, 
	view_type SMALLINT NULL, 
	visible SMALLINT NULL, 
	search_indexed SMALLINT NULL, 
	xml_search_indexed SMALLINT NULL DEFAULT '1', 
	display_link_name VARCHAR(255) NULL, 
	link_description VARCHAR(255) NULL, 
	url_link_name VARCHAR(255) NULL, 
	approved_link_name VARCHAR(255) NULL, 
	view_level VARCHAR(10) NULL, 
	view_index VARCHAR(10) NULL, 
	online_start BIGINT NULL, 
	online_stop BIGINT NULL, 
	reference VARCHAR(255) NULL, 
	view_document_id_fk INTEGER NULL, 
	online_state SMALLINT NULL DEFAULT '0', 
	deploy_command SMALLINT NULL DEFAULT '0', 
	meta_data ntext NULL, 
	meta_description ntext NULL, 
	create_date BIGINT NULL DEFAULT '0', 
	last_modified_date BIGINT NULL DEFAULT '0', 
	realm2vc_id_fk INTEGER NULL, 
	display_settings SMALLINT NULL DEFAULT '0', 
	user_last_modified_date BIGINT NULL DEFAULT '0', 
	PRIMARY KEY CLUSTERED(view_component_id)
);
CREATE INDEX idx_viewcomponent_parent_id_fk ON viewcomponent (parent_id_fk);
CREATE INDEX idx_viewcomponent_first_child_id_fk ON viewcomponent (first_child_id_fk);
CREATE INDEX idx_viewcomponent_prev_node_id_fk ON viewcomponent (prev_node_id_fk);
CREATE INDEX idx_viewcomponent_next_node_id_fk ON viewcomponent (next_node_id_fk);
CREATE INDEX idx_viewcomponent_unit_id_fk ON viewcomponent (unit_id_fk);
CREATE INDEX idx_viewcomponent_display_link_name ON viewcomponent (display_link_name);
CREATE INDEX idx_viewcomponent_reference ON viewcomponent (reference);
CREATE INDEX idx_viewcomponent_view_document_id_fk ON viewcomponent (view_document_id_fk);
CREATE INDEX idx_viewcomponent_realm2vc_id_fk ON viewcomponent (realm2vc_id_fk);

CREATE TABLE VIEWDOCUMENT (
	view_document_id INTEGER NOT NULL, 
	language VARCHAR(50) NULL, 
	view_type VARCHAR(50) NULL, 
	view_component_id_fk INTEGER NULL, 
	site_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(view_document_id)
);
CREATE INDEX idx_viewdocument_language ON viewdocument (language);
CREATE INDEX idx_viewdocument_view_type ON viewdocument (view_type);
CREATE INDEX idx_viewdocument_view_component_id_fk ON viewdocument (view_component_id_fk);
CREATE INDEX idx_viewdocument_site_id_fk ON viewdocument (site_id_fk);

CREATE TABLE REALM_LDAP (
	ldap_realm_id INTEGER NOT NULL, 
	realm_name VARCHAR(255) NULL, 
	ldap_url VARCHAR(255) NULL, 
	ldap_authentication_type VARCHAR(255) NULL, 
	ldap_suffix VARCHAR(255) NULL, 
	ldap_prefix VARCHAR(255) NULL, 
	login_page_id VARCHAR(255) NULL, 
	site_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(ldap_realm_id)
);

CREATE TABLE REALM_SIMPLE_PW (
	simple_pw_realm_id INTEGER NOT NULL, 
	realm_name VARCHAR(255) NULL, 
	login_page_id VARCHAR(255) NULL, 
	owner_id_fk VARCHAR(255) NULL, 
	site_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(simple_pw_realm_id)
);

CREATE TABLE REALM_SIMPLE_PW_USER (
	simple_pw_realm_user_id INTEGER NOT NULL, 
	user_name VARCHAR(255) NULL, 
	password VARCHAR(255) NULL, 
	roles VARCHAR(1024) NULL, 
	simple_pw_realm_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(simple_pw_realm_user_id)
);

CREATE TABLE REALM_JDBC (
	jdbc_realm_id INTEGER NOT NULL, 
	statement_user VARCHAR(255) NULL, 
	statement_role_per_user VARCHAR(255) NULL, 
	jndi_name VARCHAR(255) NULL, 
	realm_name VARCHAR(255) NULL, 
	login_page_id VARCHAR(255) NULL, 
	site_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(jdbc_realm_id)
);

CREATE TABLE REALM_JAAS (
	jaas_realm_id INTEGER NOT NULL, 
	realm_name VARCHAR(255) NULL, 
	login_page_id VARCHAR(255) NULL, 
	jaas_policy_name VARCHAR(255) NULL, 
	site_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(jaas_realm_id)
);

CREATE TABLE REALM2VIEW_COMPONENT (
	realm2view_component_id INTEGER NOT NULL, 
	role_needed VARCHAR(255) NULL, 
	simple_pw_realm_id_fk INTEGER NULL, 
	jdbc_realm_id_fk INTEGER NULL, 
	ldap_realm_id_fk INTEGER NULL, 
	jaas_realm_id_fk INTEGER NULL, 
	view_component_id_fk INTEGER NULL, 
	login_page_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(realm2view_component_id)
);

CREATE TABLE SHORT_LINK (
	short_link_id INTEGER NOT NULL, 
	short_link VARCHAR(255) NULL, 
	redirect_url VARCHAR(255) NULL, 
	site_id_fk INTEGER NULL, 
	view_document_id_fk INTEGER NULL, 
	PRIMARY KEY CLUSTERED(short_link_id)
);
CREATE INDEX idx_short_link_site_id_fk ON short_link (site_id_fk);
CREATE INDEX idx_short_link_view_document_id_fk ON short_link (view_document_id_fk);

DELETE FROM keygen;

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
INSERT INTO site (root_unit_id_fk, site_id, site_short, site_name) VALUES ('1', '1', 'test', 'www.test.de');

-- create a unit
INSERT INTO unit (unit_id, last_modified_date, name, site_id_fk) VALUES ('1', '1', 'erste rootunit', '1');

-- create a new user (username=admin, password=123)
INSERT INTO usr (user_id, passwd, masterRoot, active_site_id_fk, email, last_name, first_name, login_date) VALUES ('admin', 'QL0AFWMIX8NRZTKeof9cXsvbvu8=', '1', '1', 'email@adresse.de', 'Mustermann', 'Max', '0');

-- link the user with the unit
INSERT INTO units2users (users_id_fk, units_id_fk) VALUES ('admin','1');

-- link the user with the site
INSERT INTO sites2users (users_id_fk, sites_id_fk) VALUES ('admin','1');