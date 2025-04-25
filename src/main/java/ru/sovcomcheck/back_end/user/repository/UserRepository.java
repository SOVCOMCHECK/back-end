package ru.sovcomcheck.back_end.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.sovcomcheck.back_end.user.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
