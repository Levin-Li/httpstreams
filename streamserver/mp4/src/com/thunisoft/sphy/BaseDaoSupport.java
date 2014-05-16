package com.thunisoft.sphy;



import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class BaseDaoSupport extends HibernateDaoSupport {

    public <T> T get(Class<T> type, Serializable id) {
        return getHibernateTemplate().get(type, id);
    }

    public <T> void save(T instance) {
        getHibernateTemplate().save(instance);
    }
    
    
    public <T> void update(T instance) {
        getHibernateTemplate().update(instance);
    }
 

    public <T> void saveOrUpdate(T instance) {
        getHibernateTemplate().saveOrUpdate(instance);
    }

    public void saveOrUpdateAll(Collection<?> instance) {
        getHibernateTemplate().saveOrUpdateAll(instance);
    }
    
    public Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public <T> T delete(final Class<T> type, final Serializable id) {
        return getHibernateTemplate().execute(new HibernateCallback<T>() {
            public T doInHibernate(Session session) throws HibernateException, SQLException {
                Object instance = (Object) session.get(type, id);
                if (null != instance) {
                    session.delete(instance);
                }

                return (T)instance;
            }
        });
    }

    private static void setParamter(Query query, int position, final Object param) {
        if (param instanceof String) {
            query.setString(position, (String) param);
        } else if (param instanceof Integer) {
            query.setInteger(position, (Integer) param);
        } else if (param instanceof Short) {
            query.setShort(position, (Short) param);
        } else if (param instanceof Timestamp) {
            query.setTimestamp(position, (Timestamp) param);
        } else if (param instanceof Date) {
            query.setDate(position, (Date) param);
        } else if (null != param) {
            throw new IllegalArgumentException("unsupported type [" + param.getClass() + "]");
        } else {
            throw new IllegalArgumentException("unknown type for null");
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> executeFind(final String hql, final Object param) {
        return getHibernateTemplate().executeFind(new HibernateCallback<List<?>>() {
            public List<?> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);

                setParamter(query, 0, param);

                return query.list();
            }
        });
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> executeFind(final String hql, final Object param1, final Object param2) {
        return getHibernateTemplate().executeFind(new HibernateCallback<List<?>>() {
            public List<?> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);

                setParamter(query, 0, param1);
                setParamter(query, 1, param1);

                return query.list();
            }
        });
    }

    public BaseDaoSupport() {
        super();
    }

}
