INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');

INSERT INTO category (name) VALUES ('Art');
INSERT INTO category (name) VALUES ('Design');
INSERT INTO category (name) VALUES ('Game');

INSERT INTO ticket_type (name) VALUES ('Pay');

INSERT INTO users(is_deleted, is_organizer, is_validated, roles_role_id, email, first_name, last_name, password, phone, username, e_state_organiser)
	VALUES (false, true, true, 2, 'admin@chapchapticket.com', 'Admin', 'ChapChap', '$2a$10$YZFklL6/aTg52p46V6EOHuPbwe.w6JHoix8xbrUiLJTniSP3E6zyy', '690926957', 'admin', 'ORGANISER');