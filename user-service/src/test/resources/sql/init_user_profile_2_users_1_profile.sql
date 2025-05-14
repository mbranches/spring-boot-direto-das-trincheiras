insert into user (id, email,first_name,last_name) values (1, 'marcus@gmail.com','Marcus','Branches');
insert into user (id, email,first_name,last_name) values (2, 'vinicius@gmail.com','Vinicius','Lima');
insert into profile (id, name, description) values (1, 'admin', 'administrator');
insert into user_profile (id, profile_id, user_id) values (1, 1, 1);
insert into user_profile (id, profile_id, user_id) values        (2, 1, 2);