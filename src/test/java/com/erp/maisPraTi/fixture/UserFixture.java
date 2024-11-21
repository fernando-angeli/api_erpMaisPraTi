package com.erp.maisPraTi.fixture;

import com.erp.maisPraTi.enums.PartyStatus;
import com.erp.maisPraTi.model.Role;
import com.erp.maisPraTi.model.User;

import java.time.LocalDateTime;

public class UserFixture {

    public static Role roleAdmin(){
        Role role = new Role();
        role.setId(1L);
        role.setAuthority("ROLE_ADMIN");
        return role;
    }

    public static Role roleOperator(){
        Role role = new Role();
        role.setId(2L);
        role.setAuthority("ROLE_OPERATOR");
        return role;
    }

    public static User userAdmin (){
        User user = new User();
        user.setId(1L);
        user.setFullName("Admin Admin");
        user.setEmail("admin@admin.com");
        user.setCpf("000.111.222.333-44");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setPassword("12345");
        user.getRoles().add(roleAdmin());
        user.setStatus(PartyStatus.ACTIVE);
        return user;
    }
    public static User userOperator (){
        User user = new User();
        user.setId(1L);
        user.setFullName("Operator Operator");
        user.setEmail("operator@operator.com");
        user.setCpf("000.111.222.333-55");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setPassword("12345");
        user.getRoles().add(roleOperator());
        user.setStatus(PartyStatus.ACTIVE);
        return user;
    }

    public static User inactiveUser (){
        User user = new User();
        user.setId(1L);
        user.setFullName("Operator Operator");
        user.setEmail("operator@operator.com");
        user.setCpf("000.111.222.333-55");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setPassword("12345");
        user.getRoles().add(roleOperator());
        user.setStatus(PartyStatus.INACTIVE);
        return user;
    }

    public static User suspendedUser (){
        User user = new User();
        user.setId(1L);
        user.setFullName("Operator Operator");
        user.setEmail("operator@operator.com");
        user.setCpf("000.111.222.333-55");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setPassword("12345");
        user.getRoles().add(roleOperator());
        user.setStatus(PartyStatus.SUSPENDED);
        return user;
    }

}
