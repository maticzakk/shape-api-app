package pl.kurs.shapeapiapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.shapeapiapp.dto.UserDto;
import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.model.User;

public interface IUserService {
    User createUser(UserSignDto signDto);
    Page<UserDto> getAllUsers(Pageable pageable);
}
