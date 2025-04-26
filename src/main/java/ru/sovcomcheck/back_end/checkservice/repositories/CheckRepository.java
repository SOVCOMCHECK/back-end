package ru.sovcomcheck.back_end.checkservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.sovcomcheck.back_end.checkservice.documents.CheckDocument;

public interface CheckRepository extends MongoRepository<CheckDocument, String> {
    Page<CheckDocument> findByUserId(String userId, Pageable pageable);
}
