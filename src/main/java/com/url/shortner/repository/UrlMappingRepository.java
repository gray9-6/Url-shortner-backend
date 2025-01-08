package com.url.shortner.repository;

import com.url.shortner.model.UrlMapping;
import com.url.shortner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping,Long> {

    Optional<UrlMapping> findByShortUrl(String shortUrl);

//    @Query("""
//            SELECT um
//            FROM UrlMapping um
//            WHERE um.user.id = :userId
//            """)
//    List<UrlMapping> findByUserId(Long userId);


    List<UrlMapping> findByUser(User user);
}
