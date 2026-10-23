package com.substring.auth.service.impl;
import com.substring.auth.constant.Provider;
import com.substring.auth.dto.UserDto;
import com.substring.auth.entity.User;
import com.substring.auth.exceptions.ResourceNotFoundException;
import com.substring.auth.helper.UserHelpler;
import com.substring.auth.repository.UserRepository;
import com.substring.auth.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.substring.auth.exceptions.IllegalArgumentException;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
       if(userDto.getEmail() == null || userDto.getEmail().isBlank()){
           log.info("email is required try agian ! : ");
           throw new IllegalArgumentException("Email is required");
       }
       if (userRepository.existsByEmail(userDto.getEmail())){
           throw new IllegalArgumentException("Email is already exists !!");
        }
       // if you have extra check --put here
        User user = modelMapper.map(userDto, User.class);
       user.setProvider(userDto.getProvider()!=null? userDto.getProvider() : Provider.LOCAL);
       // role assign here to user ---for authorization
        //TODO:
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) throws ResourceNotFoundException {
         User user = userRepository.findByEmail(email)
                  .orElseThrow(() -> new ResourceNotFoundException("user not found with given email id: "+ email));
         return modelMapper.map(user, UserDto.class);
    }


    @Override
    @CacheEvict(value = "users", key = "#userId")
    public UserDto updateUser(UserDto userDto, String userId) throws ResourceNotFoundException {
        UUID uuid = UserHelpler.parseUUID(userId);

/**       User existingUser = userRepository
               .findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given user Id " + uuid));
        // we are not going to change email id for this project
        if (userDto.getName() != null) existingUser.setName(userDto.getName());
        if (userDto.getImage() != null) existingUser.setImage(userDto.getImage());
        if (userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        //TODO: change password updation logic ...
        if (userDto.getPassword() != null ) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnable(userDto.isEnable());
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class); */
       // production level coding should be as:
        User existingUser = getUser(uuid);
        Optional.ofNullable(userDto.getName())
                .filter(name -> !name.isBlank())
                .ifPresent(existingUser :: setName);
        Optional.ofNullable(userDto.getImage())
                .filter(image -> !image.isBlank())
                .ifPresent(existingUser :: setImage);
        Optional.ofNullable(userDto.getProvider())
                .ifPresent(existingUser :: setProvider);

        Optional.ofNullable(userDto.getPassword())
                .filter(password -> !password.isBlank())
                .ifPresent(existingUser :: setPassword);

         existingUser.setEnable(userDto.isEnable());
        User updatedUser = userRepository.save(existingUser);
        log.info(" user updated successfully : {} ",uuid);
        return convertToDto(updatedUser);
    }

    @Override
    public String deleteUser(String userId) throws ResourceNotFoundException {
        UUID uuid = UserHelpler.parseUUID(userId);
        User user = getUser(uuid);
           userRepository.delete(user);
           log.info("User deleted successfully : {} ",uuid);
        return "user deleted successfully !! ";
    }

    @Override
    public UserDto getUserById(String userId) throws ResourceNotFoundException {
        UUID uId = UserHelpler.parseUUID(userId);
        User user = getUser(uId);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        return userRepository.
                findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
    /**
     *  Common User Finder
     */

    private User getUser(UUID uuid) throws ResourceNotFoundException {
        return userRepository.findById(uuid)
                .orElseThrow(() ->
                        new ResourceNotFoundException("user not found with id : "+uuid));
    }

    /**
     * Dto -> Entity
     */
    private User covertToEntity(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }
    /**
     * Entity -> DTo
     */
    private UserDto convertToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
