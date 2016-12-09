package com.example.dao;

import com.example.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Integer> {

    public static final String CACHE_NAME = "cache.user";
    public static final String CACHE_NAME_PAGE = CACHE_NAME + ".page";

    @Cacheable(value = CACHE_NAME_PAGE, key = "T(java.lang.String).valueOf(#p0.pageNumber).concat('-').concat(#p0.pageSize)")
    public Page<User> findAll(Pageable pageable);

    @Cacheable(value = CACHE_NAME, key = "#p0.toString()")
    public User findOne(Integer id);

    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_NAME, key = "#p0.id.toString()"),
                    @CacheEvict(value = CACHE_NAME_PAGE, allEntries = true)
            }
    )
    public void delete(User user);

    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_NAME, key = "#p0.id.toString()"),
                    @CacheEvict(value = CACHE_NAME_PAGE, allEntries = true)
            }
    )
    public User save(User user);

}
