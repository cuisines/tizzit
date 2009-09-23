-- deletes viewdocuments that are not attached to a site
delete from viewdocument where site_id_fk not in (select site_id from site);

-- deletes not reachable viewcomponents
delete from viewcomponent WHERE parent_id_fk is null and view_component_id not in (select view_component_id_fk from viewdocument);
delete from viewcomponent WHERE view_document_id_fk not in (select view_document_id from viewdocument); 
delete from viewcomponent where unit_id_fk not in (select unit_id from unit);
--delete from viewcomponent WHERE parent_id_fk not in (select view_component_id from viewcomponent);

delete from viewdocument where view_component_id_fk not in (select view_component_id from viewcomponent); 
-- deletes units without sites
delete from unit where site_id_fk not in (select site_id from site) or site_id_fk is null;
delete from units2users where units_id_fk not in (select unit_id from unit) or users_id_fk not in (select user_id from usr);

--delete pics/docs that have a non existing unit assigned 
delete from picture where unit_id_fk not in (select unit_id from unit);
delete from document where unit_id_fk not in (select unit_id from unit); 

--delete persons/departments/adresses/talktimes without connection to a unit or to eachother
delete from persons2units where persons_Id_fk not in (select person_id from comp_person);
delete from persons2units where units_Id_fk not in (select unit_id from unit);
delete from comp_person WHERE person_id not in (select persons_id_fk from persons2units); 

delete from departments2persons where departments_id_fk not in (select department_id from comp_department);
delete from departments2persons where persons_id_fk not in (select person_id from comp_person);
delete from comp_department WHERE (department_id not in (select departments_id_fk from departments2persons)) and unit_id_fk not in (select unit_id from unit);

delete from comp_address where person_id_fk is not null and person_id_fk not in (select person_id from comp_person) or unit_id_fk is not null and unit_id_fk not in (select unit_id from unit) or department_id_fk is not null and department_id_fk not in (select department_id from comp_department);
delete from comp_talktime where person_id_fk not in (select person_id from comp_person) and person_id_fk is not null or unit_id_fk not in (select unit_id from unit) and unit_id_fk is not null or department_id_fk is not null and department_id_fk not in (select department_id from comp_department);

-- authotization entitys
delete from sgroup WHERE site_id_fk not in (select site_id from site) or site_id_fk is null; 
delete from groups2users WHERE groups_id_fk not in (select group_id from sgroup) or users_id_fk not in (select user_id from usr);
delete from groups2roles where groups_id_fk not in (select group_id from sgroup) or roles_id_fk not in (select role_id from role);
-- deletes connection from users to not existing sites and other way around
delete from sites2users where sites_id_fk not in (select site_id from site) or users_id_fk not in (select user_id from usr);
--deletes locks that have no owner anymore
delete from locks where owner_id_fk not in (select user_id from usr); 
update contentversion set lock_id_fk = null where lock_id_fk is not null and lock_id_fk not in (select lock_id from locks);

update site set site_group_id_fk = null where site_group_id_fk is not null and site_group_id_fk not in (select site_group_id from site_group);

-- REALM
delete from realm_simple_pw where owner_id_fk is null or  owner_id_fk not in (select user_id from usr) or site_id_fk is null or site_id_fk not in (select site_id from site);