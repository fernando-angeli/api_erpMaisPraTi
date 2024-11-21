package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.users.RoleDto;
import com.erp.maisPraTi.model.Role;
import com.erp.maisPraTi.repository.RoleRepository;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static com.erp.maisPraTi.util.EntityMapper.convertToDto;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Optional<RoleDto> findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não localizado: " + id));
        return Optional.of(convertToDto(role, RoleDto.class));
    }

    @Transactional(readOnly = true)
    public Page<RoleDto> findAll(Pageable pageable){
        Page<Role> roles = roleRepository.findAll(pageable);
        return roles.map(r -> convertToDto(r, RoleDto.class));
    }

}
