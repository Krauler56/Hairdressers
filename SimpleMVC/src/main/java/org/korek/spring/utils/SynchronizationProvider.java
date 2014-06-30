package org.korek.spring.utils;

import java.util.Map;

import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.korek.spring.controllers.admin.AdminController;

import com.google.common.collect.Maps;

public class SynchronizationProvider
{
	
	private static final Logger log = Logger.getLogger(SynchronizationProvider.class);

	private static Map<Pair<Class<?>, Long>, Pair<Object, Integer>> synchros = Maps.newHashMap();

	public static synchronized Object getSynchoObj(Class<?> clazz, long id)
	{
		Pair<Class<?>, Long> key = key(clazz, id);

		Pair<Object, Integer> value = synchros.get(key);

		if (value == null)
		{
			value = newValue(new Object(), 1);
		}
		else
		{
			Object lock = value.getValue0();
			Integer number = value.getValue1();
			value = newValue(lock, number + 1);
		}
		
		synchros.put(key, value);
		
		Object lock = value.getValue0();

		return lock;
	}
	
	public static synchronized void relaseSynchoObj(Class<?> clazz, long id)
	{
		Pair<Class<?>, Long> key = key(clazz, id);
		
		Pair<Object, Integer> value = synchros.get(key);
		
		if (value != null)
		{
			Integer number = value.getValue1();
			if(number > 1)
			{
				Object lock = value.getValue0();
				value = newValue(lock, number - 1);
				synchros.put(key, value);
			}
			else
			{
				synchros.remove(key);
			}
		}
		else
		{
			log.error("Relased synchro which isn't there for key: " + key);
		}
	}

	private static Pair<Class<?>, Long> key(Class<?> clazz, long id)
	{
		return new Pair<Class<?>, Long>(clazz, id);
	}

	private static Pair<Object, Integer> newValue(Object lock, Integer number)
	{
		return new Pair<Object, Integer>(lock, number);
	}
}
