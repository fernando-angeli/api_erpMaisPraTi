package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.users.*;
import com.erp.maisPraTi.fixture.UserFixture;
import com.erp.maisPraTi.model.User;
import com.erp.maisPraTi.repository.UserRepository;
import com.erp.maisPraTi.service.exceptions.DatabaseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    private User user;
    private CardDto cardDto;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;  // Mock de RoleService

    @Mock
    private BCryptPasswordEncoder passwordEncoder;  // Mock do BCryptPasswordEncoder


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = UserFixture.userAdmin();  // Certifique-se de que o UserFixture esteja correto


// Criação do CardDto usando o construtor padrão e definindo os valores com setters
        cardDto = new CardDto();  // Usando o construtor sem parâmetros
        cardDto.setSlot1("cardSlot1");  // Usando o setter para definir slot1
        cardDto.setSlot2("cardSlot2");  // Usando o setter para definir slot2
        cardDto.setSlot3("cardSlot3");  // Usando o setter para definir slot3
    }

    @Test
    void deveInserirUsuarioComSucesso() {
        UserInsertDto userInsertDto = new UserInsertDto();
        userInsertDto.setEmail("admin@admin.com");
        userInsertDto.setPassword("12345");
        userInsertDto.setFullName("Admin Admin");
        userInsertDto.setRoles(List.of(new RoleDto(1L, "ROLE_ADMIN")));  // Mock RoleDto
        userInsertDto.setCpf("000.111.222.333-44");

        when(userRepository.findByEmail(userInsertDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        UserDto userDto = userService.insert(userInsertDto);

        assertNotNull(userDto);
        assertEquals(userDto.getFullName(), "Admin Admin");
        assertEquals(userDto.getEmail(), "admin@admin.com");
        verify(userRepository, times(1)).save(any(User.class));  // Verifica se o método save foi chamado
    }

    @Test
    void naoDeveInserirUsuarioComEmailJaExistente() {
        UserInsertDto userInsertDto = new UserInsertDto();
        userInsertDto.setEmail("admin@admin.com");

        when(userRepository.findByEmail(userInsertDto.getEmail())).thenReturn(Optional.of(user));

        assertThrows(DatabaseException.class, () -> userService.insert(userInsertDto));
    }

    @Test
    void deveEncontrarUsuarioPorId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDto> userDto = userService.findById(1L);

        assertTrue(userDto.isPresent());
        assertEquals(userDto.get().getId(), 1L);
    }

    @Test
    void naoDeveEncontrarUsuarioPorId() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("updated@admin.com");
        userUpdateDto.setPassword("newPassword");
        userUpdateDto.setFullName("Updated Admin");
        userUpdateDto.setRoles(List.of(new RoleDto(1L, "ROLE_ADMIN")));  // Mock RoleDto

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        UserDto userDto = userService.update(1L, userUpdateDto);

        assertNotNull(userDto);
        assertEquals(userDto.getFullName(), "Updated Admin");
        verify(userRepository, times(1)).save(any(User.class));  // Verifica se o método save foi chamado
    }

    @Test
    void naoDeveAtualizarUsuarioComIdNaoExistente() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();

        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.update(1L, userUpdateDto));
    }

    @Test
    void deveRetornarListaDeUsuariosComPaginacao() {
        // Preparando os dados de teste
        User user1 = UserFixture.userAdmin();
        User user2 = UserFixture.userAdmin();  // Criação de um segundo usuário de teste
        List<User> userList = List.of(user1, user2);

        // Paginação
        Pageable pageable = PageRequest.of(0, 2);  // Página 0, com 2 itens por página

        // Criando a página mockada com o conteúdo de usuários
        Page<User> usersPage = new PageImpl<>(userList, pageable, userList.size());

        // Simulando o comportamento do repositório
        when(userRepository.findAll(pageable)).thenReturn(usersPage);

        // Chamando o método findAll
        Page<UserDto> userDtoPage = userService.findAll(pageable);

        // Validando o resultado
        assertNotNull(userDtoPage);
        assertEquals(2, userDtoPage.getContent().size());  // Esperamos 2 usuários na página
        assertEquals("Admin Admin", userDtoPage.getContent().get(0).getFullName());
        assertEquals("Admin Admin", userDtoPage.getContent().get(1).getFullName());
        assertEquals(1, userDtoPage.getTotalPages());  // A página total é 1, pois temos 2 usuários e 2 itens por página

        // Verificando se o repositório foi chamado com a página correta
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void deveExcluirUsuarioComSucesso() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);  // Verifica se o método deleteById foi chamado
    }

    @Test
    void naoDeveAtualizarUsuarioQuandoHouverErroDeIntegridade() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("updated@admin.com");
        userUpdateDto.setPassword("newPassword");
        userUpdateDto.setFullName("Updated Admin");
        userUpdateDto.setRoles(List.of(new RoleDto(1L, "ROLE_ADMIN")));  // Mock RoleDto

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        // Verifica se a exceção DatabaseException é lançada ao tentar atualizar com erro de integridade
        assertThrows(DatabaseException.class, () -> userService.update(1L, userUpdateDto));
    }

    @Test
    void naoDeveExcluirUsuarioQuandoHouverErroDeIntegridade() {
        // Simulando a tentativa de exclusão de um usuário que pode estar vinculado a outros registros
        when(userRepository.existsById(1L)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(userRepository).deleteById(1L);

        // Verifica se a exceção DatabaseException é lançada quando há erro de integridade
        assertThrows(DatabaseException.class, () -> userService.deleteById(1L));
    }

    @Test
    void naoDeveExcluirUsuarioQuandoHouverErroInesperado() {
        // Simulando a tentativa de exclusão de um usuário com erro inesperado
        when(userRepository.existsById(1L)).thenReturn(true);
        doThrow(RuntimeException.class).when(userRepository).deleteById(1L);  // Simula erro inesperado

        // Verifica se a exceção DatabaseException é lançada ao tentar excluir o usuário com erro inesperado
        assertThrows(DatabaseException.class, () -> userService.deleteById(1L));
    }


    @Test
    void naoDeveAtualizarUsuarioQuandoHouverErroInesperado() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("updated@admin.com");
        userUpdateDto.setPassword("newPassword");
        userUpdateDto.setFullName("Updated Admin");
        userUpdateDto.setRoles(List.of(new RoleDto(1L, "ROLE_ADMIN")));  // Mock RoleDto

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);  // Simulando erro inesperado

        // Verifica se a exceção DatabaseException é lançada ao tentar atualizar com erro inesperado
        assertThrows(DatabaseException.class, () -> userService.update(1L, userUpdateDto));
    }

    @Test
    void naoDeveExcluirUsuarioComIdNaoExistente() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(1L));
    }

    @Test
    void deveInserirCartaoParaUsuario() {
        // Criação do usuário mock
        user = new User();
        user.setId(1L);

        // Criação do CardDto e definindo os valores com os setters
        cardDto = new CardDto();  // Usando o construtor sem parâmetros gerado pelo Lombok
        cardDto.setSlot1("cardSlot1");  // Usando o setter para definir o slot1
        cardDto.setSlot2("cardSlot2");  // Usando o setter para definir o slot2
        cardDto.setSlot3("cardSlot3");  // Usando o setter para definir o slot3

        // Configuração do mock do repositório para retornar um usuário
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Chamando o método a ser testado
        userService.insertCard(1L, cardDto);

        // Verificando se os métodos foram chamados corretamente
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);

        // Verificando se os cartões foram corretamente atribuídos
        assertEquals("cardSlot1", user.getCards().get("slot1"));
        assertEquals("cardSlot2", user.getCards().get("slot2"));
        assertEquals("cardSlot3", user.getCards().get("slot3"));
    }

}


