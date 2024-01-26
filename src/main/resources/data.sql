-- MODULES
INSERT INTO modules (base_path, name) VALUES ('/users', 'USERS');
INSERT INTO modules (base_path, name) VALUES ('/auth', 'AUTH');
INSERT INTO modules (base_path, name) VALUES ('/vehicles', 'VEHICLES');
INSERT INTO modules (base_path, name) VALUES ('/routes', 'ROUTES');
INSERT INTO modules (base_path, name) VALUES ('/coordinates', 'COORDINATES');
INSERT INTO modules (base_path, name) VALUES ('/', 'WEBSOCKET');
INSERT INTO modules (base_path, name) VALUES ('/notices', 'NOTICES');
INSERT INTO modules (base_path, name) VALUES ('/companies', 'COMPANIES');

-- ROLES
INSERT INTO roles (name) VALUES ('ADMINISTRATOR');

-- OPERATIONS
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', true, 'SIGNUP', '/signup', 1);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_USERS', '', 1);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_USER', '/[0-9]*', 1);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_USER', '', 1);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_USER', '/[0-9]*', 1);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_USER', '/[0-9]*', 1);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', true, 'AUTHENTICATE', '/login', 2);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', true, 'VALIDATE_TOKEN', '/validate-token', 2);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_MY_PROFILE', '/profile', 2);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', true, 'LOGOUT', '/logout', 2);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_VEHICLES', '', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_VEHICLE', '/[0-9]*', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_VEHICLE', '/create', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_VEHICLE', '/[0-9]*', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_VEHICLE', '/[0-9]*', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_ROUTES', '', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_ROUTE', '/[0-9]*', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_ROUTE', '', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_ROUTE', '/[0-9]*', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_ROUTE', '/[0-9]*', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_COORDINATES', '', 5);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_COORDINATE', '/[0-9]*', 5);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_COORDINATE', '', 5);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_COORDINATE', '/[0-9]*', 5);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_COORDINATE', '/[0-9]*', 5);

INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', true, 'GREETING', 'index.html', 6);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', true, 'JS', 'app.js', 6);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', true, 'WEBSOCKET', 'websocket', 6);

INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_NOTICES', '', 7);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_NOTICE', '/[0-9]*', 7);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_NOTICE', '', 7);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_NOTICE', '/[0-9]*', 7);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_NOTICE', '/[0-9]*', 7);

INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_COMPANIES', '', 8);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_COMPANY', '/[0-9]*', 8);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_COMPANY', '', 8);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_COMPANY', '/[0-9]*', 8);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_COMPANY', '/[0-9]*', 8);

-- PERMISSIONS
INSERT INTO permissions (operation_id, role_id) VALUES (1, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (2, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (3, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (4, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (5, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (6, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (7, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (8, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (9, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (10, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (11, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (12, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (13, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (14, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (15, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (16, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (17, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (18, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (19, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (20, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (21, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (22, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (23, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (24, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (25, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (26, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (27, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (28, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (29, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (30, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (31, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (32, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (33, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (34, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (35, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (36, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (37, 1);
INSERT INTO permissions (operation_id, role_id) VALUES (38, 1);

INSERT INTO users (
                   current_country_id,
                   document_number,
                   role_id,
                   birthdate,
                   created_at,
                   deleted_at,
                   email,
                   first_name,
                   full_name,
                   gender,
                   image,
                   last_name,
                   password,
                   phone,
                   second_name,
                   updated_at,
                   username,
                   document_type
) VALUES (
          null,
          1006872911,
          1,
          '1990-01-01',
          NOW(),
          null,
          'esperar.app@example.com',
          'Esperar',
          'Esperar App',
          'M',
          null,
          'App',
          '$2a$10$kCmHOhZn/XOM/C06Qd22LeyXhUTqtp/B4947mEcJViprF.geroDRK',
          '3123097192',
          null,
          null,
          'esperar-app-test',
          'CC'

)