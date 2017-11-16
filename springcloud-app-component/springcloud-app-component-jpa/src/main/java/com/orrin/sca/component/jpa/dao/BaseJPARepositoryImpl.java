
package com.orrin.sca.component.jpa.dao;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * @author orrin.zhang on 2017/8/3.
 * 使用方法：@EnableJpaRepositories(repositoryBaseClass = BaseJPARepositoryImpl.class)
 */
public class BaseJPARepositoryImpl<E, PK extends Serializable> extends SimpleJpaRepository<E, PK> implements
		BaseJPARepository<E, PK> {
    private final EntityManager entityManager;

    public BaseJPARepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }



    @Override
    public Page<E> queryByExampleWithRange(Example example, List<Range<E>> ranges, Pageable pageable) {
        Specification<E> byExample = new ByExampleSpecification<>(example);
        Specification<E> byRanges = new ByRangeSpecification<>(ranges);
        return findAll(where(byExample).and(byRanges),pageable);
    }


}
