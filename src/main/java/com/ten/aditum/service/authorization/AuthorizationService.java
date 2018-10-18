package com.ten.aditum.service.authorization;

/**
 * AuthorizationService :权限控制
 *
 * @author Administrator
 */
public interface AuthorizationService {

    boolean verifyLogin(String username, String password);

    boolean verifyRole(String role);

    boolean verifyPermission(String permission);
}
