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
INSERT INTO roles (name) VALUES ('CEO');
INSERT INTO roles (name) VALUES ('DRIVER');

-- AUTH OPERATIONS
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

-- VEHICLES OPERATIONS
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_VEHICLES', '', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_VEHICLE', '/[0-9]*', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_VEHICLE', '/create', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_VEHICLE', '/[0-9]*', 3);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_VEHICLE', '/[0-9]*', 3);

-- ROUTES OPERATIONS
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_ROUTES', '', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_ROUTE', '/[0-9]*', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_ROUTE', '', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_ROUTE', '/[0-9]*', 4);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_ROUTE', '/[0-9]*', 4);

-- COORDINATES OPERATIONS
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_COORDINATES', '', 5);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_COORDINATE', '/[0-9]*', 5);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_COORDINATE', '', 5);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_COORDINATE', '/[0-9]*', 5);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_COORDINATE', '/[0-9]*', 5);

-- WEBSOCKET OPERATIONS
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', true, 'GREETING', 'index.html', 6);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', true, 'JS', 'app.js', 6);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', true, 'WEBSOCKET', 'websocket', 6);

-- NOTICES OPERATIONS
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_NOTICES', '', 7);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_NOTICE', '/[0-9]*', 7);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_NOTICE', '', 7);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_NOTICE', '/[0-9]*', 7);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_NOTICE', '/[0-9]*', 7);

-- COMPANIES OPERATIONS
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ALL_COMPANIES', '', 8);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'READ_ONE_COMPANY', '/[0-9]*', 8);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('POST', false, 'CREATE_ONE_COMPANY', '', 8);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('PUT', false, 'UPDATE_ONE_COMPANY', '/[0-9]*', 8);
INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('DELETE', false, 'REMOVE_ONE_COMPANY', '/[0-9]*', 8);

INSERT INTO operations (http_method, is_public, name, path, module_id) VALUES ('GET', false, 'DRIVERS_BY_COMPANY', '/by-company/[0-9]*', 1);

-- ADMINISTRATOR PERMISSIONS
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
INSERT INTO permissions (operation_id, role_id) VALUES (39, 1);

-- CEO PERMISSIONS
INSERT INTO permissions (operation_id, role_id) VALUES (1, 2); -- SIGNUP
INSERT INTO permissions (operation_id, role_id) VALUES (2, 2); -- READ_ALL_USERS
INSERT INTO permissions (operation_id, role_id) VALUES (3, 2); -- READ_ONE_USER
INSERT INTO permissions (operation_id, role_id) VALUES (4, 2); -- CREATE_ONE_USER
INSERT INTO permissions (operation_id, role_id) VALUES (7, 2);  -- AUTHENTICATE
INSERT INTO permissions (operation_id, role_id) VALUES (8, 2);  -- VALIDATE_TOKEN
INSERT INTO permissions (operation_id, role_id) VALUES (9, 2);  -- READ_MY_PROFILE
INSERT INTO permissions (operation_id, role_id) VALUES (10, 2); -- LOGOUT
INSERT INTO permissions (operation_id, role_id) VALUES (11, 2); -- READ_ALL_VEHICLES
INSERT INTO permissions (operation_id, role_id) VALUES (12, 2); -- READ_ONE_VEHICLE
INSERT INTO permissions (operation_id, role_id) VALUES (13, 2); -- CREATE_ONE_VEHICLE
INSERT INTO permissions (operation_id, role_id) VALUES (14, 2); -- UPDATE_ONE_VEHICLE
INSERT INTO permissions (operation_id, role_id) VALUES (15, 2); -- REMOVE_ONE_VEHICLE
INSERT INTO permissions (operation_id, role_id) VALUES (16, 2); -- READ_ALL_ROUTES
INSERT INTO permissions (operation_id, role_id) VALUES (17, 2); -- READ_ONE_ROUTE
INSERT INTO permissions (operation_id, role_id) VALUES (18, 2); -- CREATE_ONE_ROUTE
INSERT INTO permissions (operation_id, role_id) VALUES (19, 2); -- UPDATE_ONE_ROUTE
INSERT INTO permissions (operation_id, role_id) VALUES (20, 2); -- REMOVE_ONE_ROUTE
INSERT INTO permissions (operation_id, role_id) VALUES (21, 2); -- READ_ALL_COORDINATES
INSERT INTO permissions (operation_id, role_id) VALUES (22, 2); -- READ_ONE_COORDINATE
INSERT INTO permissions (operation_id, role_id) VALUES (23, 2); -- CREATE_ONE_COORDINATE
INSERT INTO permissions (operation_id, role_id) VALUES (24, 2); -- UPDATE_ONE_COORDINATE
INSERT INTO permissions (operation_id, role_id) VALUES (25, 2); -- REMOVE_ONE_COORDINATE
INSERT INTO permissions (operation_id, role_id) VALUES (26, 2); -- GREETING
INSERT INTO permissions (operation_id, role_id) VALUES (27, 2); -- JS
INSERT INTO permissions (operation_id, role_id) VALUES (28, 2); -- WEBSOCKET
INSERT INTO permissions (operation_id, role_id) VALUES (29, 2); -- READ_ALL_NOTICES
INSERT INTO permissions (operation_id, role_id) VALUES (30, 2); -- READ_ONE_NOTICE
INSERT INTO permissions (operation_id, role_id) VALUES (31, 2); -- CREATE_ONE_NOTICE
INSERT INTO permissions (operation_id, role_id) VALUES (32, 2); -- UPDATE_ONE_NOTICE
INSERT INTO permissions (operation_id, role_id) VALUES (33, 2); -- REMOVE_ONE_NOTICE
INSERT INTO permissions (operation_id, role_id) VALUES (34, 2); -- READ_ALL_COMPANIES
INSERT INTO permissions (operation_id, role_id) VALUES (35, 2); -- READ_ONE_COMPANY
INSERT INTO permissions (operation_id, role_id) VALUES (36, 2); -- CREATE_ONE_COMPANY
INSERT INTO permissions (operation_id, role_id) VALUES (37, 2); -- UPDATE_ONE_COMPANY
INSERT INTO permissions (operation_id, role_id) VALUES (38, 2); -- REMOVE_ONE_COMPANY
INSERT INTO permissions (operation_id, role_id) VALUES (39, 2); -- DRIVERS_BY_COMPANY

-- DRIVER PERMISSIONS
INSERT INTO permissions (operation_id, role_id) VALUES (1, 3); -- SIGNUP
INSERT INTO permissions (operation_id, role_id) VALUES (2, 3); -- READ_ALL_USERS
INSERT INTO permissions (operation_id, role_id) VALUES (3, 3); -- READ_ONE_USER
INSERT INTO permissions (operation_id, role_id) VALUES (7, 3);  -- AUTHENTICATE
INSERT INTO permissions (operation_id, role_id) VALUES (8, 3);  -- VALIDATE_TOKEN
INSERT INTO permissions (operation_id, role_id) VALUES (9, 3);  -- READ_MY_PROFILE
INSERT INTO permissions (operation_id, role_id) VALUES (10, 3); -- LOGOUT
INSERT INTO permissions (operation_id, role_id) VALUES (11, 3); -- READ_ALL_VEHICLES
INSERT INTO permissions (operation_id, role_id) VALUES (12, 3); -- READ_ONE_VEHICLE
INSERT INTO permissions (operation_id, role_id) VALUES (16, 3); -- READ_ALL_ROUTES
INSERT INTO permissions (operation_id, role_id) VALUES (17, 3); -- READ_ONE_ROUTE
INSERT INTO permissions (operation_id, role_id) VALUES (21, 3); -- READ_ALL_COORDINATES
INSERT INTO permissions (operation_id, role_id) VALUES (22, 3); -- READ_ONE_COORDINATE
INSERT INTO permissions (operation_id, role_id) VALUES (26, 3); -- GREETING
INSERT INTO permissions (operation_id, role_id) VALUES (27, 3); -- JS
INSERT INTO permissions (operation_id, role_id) VALUES (28, 3); -- WEBSOCKET
INSERT INTO permissions (operation_id, role_id) VALUES (29, 3); -- READ_ALL_NOTICES
INSERT INTO permissions (operation_id, role_id) VALUES (30, 3); -- READ_ONE_NOTICE
INSERT INTO permissions (operation_id, role_id) VALUES (31, 3); -- CREATE_ONE_NOTICE
INSERT INTO permissions (operation_id, role_id) VALUES (32, 3); -- UPDATE_ONE_NOTICE
INSERT INTO permissions (operation_id, role_id) VALUES (33, 3); -- REMOVE_ONE_NOTICE
INSERT INTO permissions (operation_id, role_id) VALUES (39, 3); -- DRIVERS_BY_COMPANY


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
          'esperar.app@admin.com',
          'Admin',
          'Admin User',
          'M',
          null,
          'User',
          '$2a$10$kCmHOhZn/XOM/C06Qd22LeyXhUTqtp/B4947mEcJViprF.geroDRK', -- Secret123.
          '3123097192',
          null,
          null,
          'esperar-app-admin',
          'CC'

);

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
             1006872912,
             2,
             '1992-02-02',
             NOW(),
             null,
             'esperar.app@ceo.com',
             'CEO',
             'CEO User',
             'M',
             null,
             'User',
             '$2a$10$kCmHOhZn/XOM/C06Qd22LeyXhUTqtp/B4947mEcJViprF.geroDRK', -- Secret123.
             '3123097193',
             null,
             null,
             'esperar-app-ceo',
             'CC'
);

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
             1006872913,
             3,
             '1993-03-03',
             NOW(),
             null,
             'esperar.app@driver.com',
             'Driver',
             'Driver User',
             'M',
             null,
             'User',
             '$2a$10$kCmHOhZn/XOM/C06Qd22LeyXhUTqtp/B4947mEcJViprF.geroDRK', -- Secret123.
             '3123097194',
             null,
             null,
             'esperar-app-driver',
             'CC'
);