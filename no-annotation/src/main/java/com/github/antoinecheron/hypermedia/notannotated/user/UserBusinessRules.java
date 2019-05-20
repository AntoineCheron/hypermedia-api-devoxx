package com.github.antoinecheron.hypermedia.notannotated.user;

import java.util.List;
import java.util.Optional;

public class UserBusinessRules {

  private static final UserRole DEFAULT_ROLE_WHEN_USER_CREATED_BY_ADVISOR = UserRole.CLIENT;
  private static final UserRole DEFAULT_ROLE_WHEN_USER_CREATED_BY_MANAGER = UserRole.CLIENT;
  private static final UserRole DEFAULT_ROLE_WHEN_USER_CREATE_BY_UNKNOWN = UserRole.CLIENT;

  private static final List<UserRole> ROLES_OF_NEW_USER_AN_ADVISOR_CAN_CREATE = List.of(UserRole.BANK_ADVISOR, UserRole.CLIENT);
  private static final List<UserRole> ROLES_OF_NEW_USER_A_MANAGER_CAN_CREATE = List.of(UserRole.BANK_MANAGER, UserRole.BANK_ADVISOR, UserRole.CLIENT);
  private static final List<UserRole> ROLES_OF_NEW_USER_ANYONE_CAN_CREATE = List.of(UserRole.CLIENT);

  public static boolean isRoleAssignmentAuthorized(User creator, UserCreationFormWithRole creationForm) {
    if (UserRole.BANK_ADVISOR.equals(creator.getRole())) {
      return ROLES_OF_NEW_USER_AN_ADVISOR_CAN_CREATE.contains(creationForm.getRole());
    } else if (UserRole.BANK_MANAGER.equals(creator.getRole())) {
      return ROLES_OF_NEW_USER_A_MANAGER_CAN_CREATE.contains(creationForm.getRole());
    } else {
      return ROLES_OF_NEW_USER_ANYONE_CAN_CREATE.contains(creationForm.getRole());
    }
  }

  public static UserRole getDefaultRoleToAssign(Optional<UserRole> maybeCreatorRole) {
    return maybeCreatorRole.map(role -> {
      if (UserRole.BANK_ADVISOR.equals(role)) {
        return DEFAULT_ROLE_WHEN_USER_CREATED_BY_ADVISOR;
      } else if (UserRole.BANK_MANAGER.equals(role)) {
        return DEFAULT_ROLE_WHEN_USER_CREATED_BY_MANAGER;
      } else {
        return DEFAULT_ROLE_WHEN_USER_CREATE_BY_UNKNOWN;
      }
    }).orElse(DEFAULT_ROLE_WHEN_USER_CREATE_BY_UNKNOWN);
  }

}
