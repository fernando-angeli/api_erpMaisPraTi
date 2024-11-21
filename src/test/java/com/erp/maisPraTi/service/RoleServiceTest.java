package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.users.RoleDto;
import com.erp.maisPraTi.model.Role;
import com.erp.maisPraTi.repository.RoleRepository;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    private Role role;
    private RoleDto roleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role(1L, "ADMIN");
        roleDto = new RoleDto(1L, "ADMIN");
    }

    @Test
    void testarBuscarPorId_quandoRoleExiste() {
        // Dado
        Role role = new Role(1L, "ROLE_ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role)); // Simula retorno do repositório

        // Quando
        Optional<RoleDto> result = roleService.findById(1L);

        // Então
        assertTrue(result.isPresent()); // Verifica se o resultado existe
        assertEquals("ROLE_ADMIN", result.get().getAuthority()); // Verifica se a authority foi mapeada corretamente
        assertEquals(1L, result.get().getId()); // Verifica se o id foi mapeado corretamente

        verify(roleRepository, times(1)).findById(1L); // Verifica que a consulta foi feita uma vez
    }

    @Test
    void testarBuscarPorId_quandoRoleNaoExiste() {
        // Dado
        when(roleRepository.findById(1L)).thenReturn(Optional.empty()); // Simula retorno vazio

        // Quando & Então
        assertThrows(ResourceNotFoundException.class, () -> roleService.findById(1L)); // Verifica que a exceção é lançada

        verify(roleRepository, times(1)).findById(1L); // Verifica que a consulta foi feita uma vez
    }

    @Test
    void testarBuscarTodos_quandoRolesExistem() {
        // Dado
        Role role1 = new Role(1L, "ROLE_ADMIN");
        Role role2 = new Role(2L, "ROLE_USER");
        Page<Role> page = new PageImpl<>(Arrays.asList(role1, role2)); // Página com duas roles
        Pageable pageable = PageRequest.of(0, 10);

        when(roleRepository.findAll(pageable)).thenReturn(page); // Simula o retorno da consulta

        // Quando
        Page<RoleDto> result = roleService.findAll(pageable);

        // Então
        assertNotNull(result); // Verifica se o resultado não é nulo
        assertEquals(2, result.getContent().size()); // Verifica se a página tem 2 elementos

        // Verifica se o mapeamento ocorreu corretamente
        assertEquals("ROLE_ADMIN", result.getContent().get(0).getAuthority());
        assertEquals("ROLE_USER", result.getContent().get(1).getAuthority());

        verify(roleRepository, times(1)).findAll(pageable); // Verifica que a consulta foi chamada uma vez
    }

    @Test
    void testarBuscarTodos_quandoRolesNaoExistem() {
        // Dado
        Page<Role> page = Page.empty(); // Página vazia
        Pageable pageable = PageRequest.of(0, 10);
        when(roleRepository.findAll(pageable)).thenReturn(page); // Simula uma página vazia

        // Quando
        Page<RoleDto> result = roleService.findAll(pageable);

        // Então
        assertNotNull(result); // Verifica que o resultado não é nulo
        assertTrue(result.getContent().isEmpty()); // Verifica que a página está vazia

        verify(roleRepository, times(1)).findAll(pageable); // Verifica que a consulta foi chamada uma vez
    }

}
