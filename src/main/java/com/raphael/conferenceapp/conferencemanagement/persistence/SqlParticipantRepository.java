package com.raphael.conferenceapp.conferencemanagement.persistence;

import com.raphael.conferenceapp.conferencemanagement.entity.PaginatedItems;
import com.raphael.conferenceapp.conferencemanagement.entity.Participant;
import com.raphael.conferenceapp.conferencemanagement.entity.ParticipantQuery;
import com.raphael.conferenceapp.conferencemanagement.exception.ParticipantAlreadyRegisteredException;
import com.raphael.conferenceapp.conferencemanagement.usecase.ParticipantRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class SqlParticipantRepository implements ParticipantRepository {
    private final JpaParticipantRepository jpaRepository;

    @Override
    public PaginatedItems<Participant> findByQuery(final ParticipantQuery query) {
        Specification<ParticipantEntity> specification = (root, q, criteriaBuilder) -> {
            List<Predicate> conditions = new LinkedList<>();

            if (query.conferenceId() != null) {
                conditions.add(criteriaBuilder.equal(root.get("conferenceId"), query.conferenceId()));
            }

            if (StringUtils.isNotBlank(query.name())) {
                conditions.add(criteriaBuilder.like(root.get("name"), "%" + query.name() + "%"));
            }

            if (StringUtils.isNotBlank(query.email())) {
                conditions.add(criteriaBuilder.equal(root.get("email"), query.email()));
            }

            return criteriaBuilder.and(conditions.toArray(new Predicate[0]));
        };

        PageRequest request = PageRequest.of(
                Math.toIntExact(query.currentPage()) - 1,
                Math.toIntExact(query.limit())
        );

        Page<ParticipantEntity> page = jpaRepository.findAll(specification, request);

        return new PaginatedItems<>(
                page.get().map(ParticipantEntity::toDomain).toList(),
                query.limit(),
                page.getTotalElements(),
                query.offset()
        );
    }

    @Override
    public Participant save(final Participant participant) {
        try {
            return jpaRepository.save(ParticipantEntity.fromDomain(participant)).toDomain();
        } catch (DataIntegrityViolationException e) {
            throw new ParticipantAlreadyRegisteredException("The provided email is already registered in this conference");
        }
    }
}
