package org.korek.spring.repositories.dao.base;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDAO<T> implements IBaseDAO<T>
{
	private Class<T> clazz;

	@Autowired
	private SessionFactory sessionFactory;
	
	

	public BaseDAO(Class<T> clazz)
	{
		super();
		this.clazz = clazz;
	}

	public void setClazz(final Class<T> clazzToSet)
	{
		clazz = clazzToSet;
	}

	@SuppressWarnings("unchecked")
	public T findOne(final long id)
	{
		return (T) getCurrentSession().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll()
	{
		return getCurrentSession().createQuery("from " + clazz.getName()).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(String order)
	{
		return getCurrentSession().createQuery("from " + clazz.getName() + " x order by " + "x." + order ).list();
	}

	public void save(final T entity)
	{
		getCurrentSession().persist(entity);
	}

	@SuppressWarnings("unchecked")
	public T update(final T entity)
	{
		return (T) getCurrentSession().merge(entity);
	}

	public void delete(final T entity)
	{
		getCurrentSession().delete(entity);
	}

	public void deleteById(final long id)
	{
		final T entity = findOne(id);
		delete(entity);
	}
	
	@SuppressWarnings("unchecked")
	public T load(final long id)
	{
		return (T) getCurrentSession().load(clazz, id);
	}

	public final Session getCurrentSession()
	{
		return sessionFactory.getCurrentSession();
	}
}
