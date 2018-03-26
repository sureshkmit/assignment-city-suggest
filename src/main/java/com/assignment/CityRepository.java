package com.assignment;


import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
/**
 * City data are stored in cities table containing id and name fields. Name filed is indexed.
 * In future we can add hits count integer column and filter top atmost values
 * based on hits count.
 */
public interface CityRepository extends CrudRepository<City, Long> {
    Stream<City> findByNameStartingWithIgnoreCase(String query, Pageable pageable);
}
