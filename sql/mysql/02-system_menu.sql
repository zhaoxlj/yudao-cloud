
-- ----------------------------
-- Web管理端 - 客户端管理菜单初始化
-- ----------------------------

-- 1. 创建客户端管理目录
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('客户端管理', '', 1, 10, 0, '/app-client', 'ep:monitor', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0);

-- 假设客户端管理目录ID为 @client_manage_id
SET @client_manage_id = LAST_INSERT_ID();

-- 2. 创建客户端用户管理菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('客户端用户管理', '', 2, 1, @client_manage_id, 'app-client-user', 'ep:user', 'system/appClient/index', 'AppClientUser', 0, 1, 1, 1, 'admin', NOW(), 'admin', NOW(), 0);

SET @client_user_menu_id = LAST_INSERT_ID();

-- 3. 创建客户端用户管理按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES 
('查看用户', 'system:app-client:query', 3, 1, @client_user_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('重置密钥', 'system:app-client:reset-secret', 3, 3, @client_user_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('分配角色', 'system:app-client:assign-role', 3, 4, @client_user_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0);

-- 4. 创建客户端菜单管理菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('客户端菜单管理', '', 2, 2, @client_manage_id, 'app-client-menu', 'ep:menu', 'system/appClientMenu/index', 'AppClientMenu', 0, 1, 1, 1, 'admin', NOW(), 'admin', NOW(), 0);

SET @client_menu_menu_id = LAST_INSERT_ID();

-- 5. 创建客户端菜单管理按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES 
('查看菜单', 'system:app-client-menu:query', 3, 1, @client_menu_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('新增菜单', 'system:app-client-menu:create', 3, 2, @client_menu_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('修改菜单', 'system:app-client-menu:update', 3, 3, @client_menu_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('删除菜单', 'system:app-client-menu:delete', 3, 4, @client_menu_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0);

-- 6. 创建客户端角色管理菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('客户端角色管理', '', 2, 3, @client_manage_id, 'app-client-role', 'ep:avatar', 'system/appClientRole/index', 'AppClientRole', 0, 1, 1, 1, 'admin', NOW(), 'admin', NOW(), 0);

SET @client_role_menu_id = LAST_INSERT_ID();

-- 7. 创建客户端角色管理按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES 
('查看角色', 'system:app-client-role:query', 3, 1, @client_role_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('新增角色', 'system:app-client-role:create', 3, 2, @client_role_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('修改角色', 'system:app-client-role:update', 3, 3, @client_role_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('删除角色', 'system:app-client-role:delete', 3, 4, @client_role_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0),
('分配菜单', 'system:app-client-role:assign-menu', 3, 5, @client_role_menu_id, '', '', NULL, NULL, 0, 1, 0, 1, 'admin', NOW(), 'admin', NOW(), 0);

-- 在令牌管理页面新增了 `app-client` 客户端配置（访问令牌7天、刷新令牌30天）