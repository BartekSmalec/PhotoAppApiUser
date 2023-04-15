package com.bartek.PhotoAppApiUser.service;

import com.bartek.PhotoAppApiUser.shared.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDetails);
}
