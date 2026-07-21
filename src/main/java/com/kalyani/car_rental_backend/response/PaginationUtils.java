package com.kalyani.car_rental_backend.response;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class PaginationUtils {

    private PaginationUtils() {
    }

    public static Pageable createPageable(int page,
                                          int size,
                                          String sortBy,
                                          String sortDir,
                                          Set<String> allowedSortFields) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sortBy field. Allowed values: " + String.join(", ", allowedSortFields)
            );
        }

        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortDir);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("sortDir must be asc or desc");
        }

        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
