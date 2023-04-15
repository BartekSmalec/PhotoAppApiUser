package com.bartek.PhotoAppApiUser.service;

import com.bartek.PhotoAppApiUser.data.UserEntity;
import com.bartek.PhotoAppApiUser.repositories.UserRepository;
import com.bartek.PhotoAppApiUser.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        userRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

        return returnValue;
    }
}
