package pl.kurs.shapeapiapp.service;

import pl.kurs.shapeapiapp.dto.UserSignDto;
import pl.kurs.shapeapiapp.model.User;

public interface IUserService {
    User createUser(UserSignDto signDto);
}
