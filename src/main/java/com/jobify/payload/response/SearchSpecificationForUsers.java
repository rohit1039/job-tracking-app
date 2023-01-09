package com.jobify.payload.response;

import com.jobify.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SearchSpecificationForUsers implements Specification<User>
{
    private final SearchCriteria criteria;

    public SearchSpecificationForUsers(SearchCriteria criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate
        (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {

        if (criteria.getOperation().equalsIgnoreCase(":"))
        {
            if (root.get(criteria.getColumn()).getJavaType() == String.class)
            {
                return builder.like(
                    root.get(criteria.getColumn()), "%" + criteria.getValue() + "%");
            }
        }
        return null;
    }
}
