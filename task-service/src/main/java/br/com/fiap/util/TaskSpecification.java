package br.com.fiap.util;

import br.com.fiap.model.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

private TaskSpecification() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Task> create(final String status, final Long userId) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (status != null && !status.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("status"), status.toUpperCase()));
            }

            if(userId != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("userId"), userId));
            }

            return predicates;
        };
    }

}
