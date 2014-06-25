package org.korek.spring.repositories.dao.base;

import java.util.List;

import org.hibernate.Session;

public interface IBaseDAO<T>
{
	void setClazz(final Class<T> clazzToSet);

	T findOne(final long id);

	List<T> findAll();
	
	List<T> findAll(String order);

	void save(final T entity);

	T update(final T entity);

	void delete(final T entity);

	void deleteById(final long id);
	
	T load(final long id);

	Session getCurrentSession();
}
