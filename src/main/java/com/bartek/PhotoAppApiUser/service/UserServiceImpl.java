package com.bartek.PhotoAppApiUser.service;

import com.bartek.PhotoAppApiUser.data.AlbumsServiceClient;
import com.bartek.PhotoAppApiUser.data.UserEntity;
import com.bartek.PhotoAppApiUser.model.AlbumResponseModel;
import com.bartek.PhotoAppApiUser.repositories.UserRepository;
import com.bartek.PhotoAppApiUser.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //private RestTemplate restTemplate;

    private AlbumsServiceClient albumsServiceClient;

    private Environment environment;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RestTemplate restTemplate, Environment environment, AlbumsServiceClient albumsServiceClient) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        //this.restTemplate = restTemplate;
        this.environment = environment;
        this.albumsServiceClient = albumsServiceClient;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getEmail(), user.getEncryptedPassword()
                , true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw new UsernameNotFoundException("User not found");
/*
        String albumsUrl = String.format(environment.getProperty("albums.url"), userId);

        ResponseEntity<List<AlbumResponseModel>> albumResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
        });

        List<AlbumResponseModel> albumsList = albumResponse.getBody();*/

        Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

        logger.debug("Before calling album service");

        List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);

        logger.debug("After calling album service");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        userDto.setAlbums(albumsList);

        return userDto;
    }
}
