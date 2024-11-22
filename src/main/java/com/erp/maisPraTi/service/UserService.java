package com.erp.maisPraTi.service;

import com.erp.maisPraTi.dto.users.*;
import com.erp.maisPraTi.model.Role;
import com.erp.maisPraTi.model.User;
import com.erp.maisPraTi.repository.UserRepository;
import com.erp.maisPraTi.service.exceptions.DatabaseException;
import com.erp.maisPraTi.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.erp.maisPraTi.util.EntityMapper.convertToDto;
import static com.erp.maisPraTi.util.EntityMapper.convertToEntity;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Transactional
    public UserDto insert(UserInsertDto userInsertDto) {
        if(userRepository.findByEmail(userInsertDto.getEmail()).isPresent())
            throw new DatabaseException("E-mail já utilizado.");
        User newUser = convertToEntity(userInsertDto, User.class);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        updateRoles(userInsertDto.getRoles(), newUser);
        newUser.setPassword(passwordEncoder.encode(userInsertDto.getPassword()));
        Map<String, String> cards = new HashMap<>();
        cards.put("slot1", "item1");
        cards.put("slot2", "item2");
        cards.put("slot3", "item3");
        newUser = userRepository.save(newUser);
        return convertToDto(newUser, UserDto.class);

    }

    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não localizado: " + id));
        return Optional.of(convertToDto(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> convertToDto(user, UserDto.class));
    }

    @Transactional
    public UserDto update(Long id, UserUpdateDto userUpdateDto){
        verifyExistsId(id);
        try{
            
            User user = userRepository.getReferenceById(id);
            user.getRoles().clear();
            convertToEntity(userUpdateDto, user);
            user.setUpdatedAt(LocalDateTime.now());
            updateRoles(userUpdateDto.getRoles(), user);
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
            user.setId(id);
            user = userRepository.save(user);
            return convertToDto(user, UserDto.class);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Não foi possível fazer a alteração neste usuário.");
        } catch (Exception e) {
            throw new DatabaseException("Erro inesperado ao tentar atualizar o usuário.");
        }
    }

    @Transactional
    public void deleteById(Long id){
        verifyExistsId(id);
        try{
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Não foi possível excluir este usuário. Ele pode estar vinculado a outros registros.");
        } catch (Exception e) {
            throw new DatabaseException("Erro inesperado ao tentar excluir o usuário.");
        }
    }

    private void updateRoles(List<RoleDto> roleDtos, User user) {
        user.getRoles().clear();
        roleDtos.forEach(roleDto -> {
            Role role = convertToEntity(roleService.findById(roleDto.getId()), Role.class);
            user.getRoles().add(role);
        });
    }

    private void verifyExistsId(Long id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Id não localizado: " + id);
        }
    }

    @Transactional
    public void insertCard(Long id, CardDto cardDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não localizado."));
        user.getCards().clear();
        user.addCards("slot1", cardDto.getSlot1());
        user.addCards("slot2", cardDto.getSlot2());
        user.addCards("slot3", cardDto.getSlot3());
        userRepository.save(user);
    }

}
