package com.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class SuggestService {

    @Autowired
    private CityRepository cityRepository;

    /**
     * Provides auto completion suggestions for cities in India.
     *
     * @param start  The characters that the city starts with.
     * @param atmost Maximum number of suggestions needed.
     * @return a plain text (mime "text/plain") with each line containing one suggestion.
     */
    @Transactional(readOnly = true)
    @GetMapping(value = "/suggest_cities", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> suggest(@RequestParam String start,
                                          @RequestParam Integer atmost) {

        if (start.length() < 1 || atmost < 1) {
            return ResponseEntity.badRequest().body("invalid query param");
        }

        Sort sortOnName = new Sort(Sort.Direction.ASC, "name"); // In future, hits based sorting
        Pageable pageable = PageRequest.of(0, atmost, sortOnName);
        Stream<City> suggestions = cityRepository.findByNameStartingWithIgnoreCase(start, pageable);
        String result = suggestions.map(City::getName).collect(Collectors.joining("\n"));
        return ResponseEntity
                .ok()
                .body(result);
    }
}
