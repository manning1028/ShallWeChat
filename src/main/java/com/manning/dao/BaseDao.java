package com.manning.dao;

import com.jq.support.common.persistence.Page;
import com.jq.support.common.persistence.Parameter;
import com.jq.support.common.utils.Reflections;
import com.jq.support.common.utils.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


public class BaseDao<T> {
    @Autowired
    private SessionFactory sessionFactory;
    private final Class<?> entityClass = Reflections.getClassGenricType(this.getClass());

    public BaseDao() {
    }

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public void flush() {
        this.getSession().flush();
    }

    public void clear() {
        this.getSession().clear();
    }

    public <E> Page<E> find(Page<E> page, String qlString) {
        return this.find(page, (String)qlString, (Parameter)null);
    }

    public <E> Page<E> find(Page<E> page, String qlString, Parameter parameter) {
        String ql;
        Query query;
        if (!page.isDisabled() && !page.isNotCount()) {
            ql = "select count(*) " + this.removeSelect(this.removeOrders(qlString));
            query = this.createQuery(ql, parameter);
            List<Object> list = query.list();
            if (list.size() > 0) {
                page.setCount(Long.valueOf(list.get(0).toString()));
            } else {
                page.setCount((long)list.size());
            }

            if (page.getCount() < 1L) {
                return page;
            }
        }

        ql = qlString;
        if (StringUtils.isNotBlank(page.getOrderBy())) {
            ql = qlString + " order by " + page.getOrderBy();
        }

        query = this.createQuery(ql, parameter);
        if (!page.isDisabled()) {
            query.setFirstResult(page.getFirstResult());
            query.setMaxResults(page.getMaxResults());
        }

        page.setList(query.list());
        return page;
    }

    public <E> List<E> find(String qlString) {
        return this.find((String)qlString, (Parameter)null);
    }

    public <E> List<E> find(String qlString, Parameter parameter) {
        Query query = this.createQuery(qlString, parameter);
        return query.list();
    }

    public <E> List<E> find(String qlString, int num) {
        Query query = this.getSession().createQuery(qlString);
        query.setMaxResults(num);
        return query.list();
    }

    public List<List<Object>> getSqlList(String sql, int stateRow, int rowsPerPage) {
        Query query = this.getSession().createSQLQuery(sql);
        query.setFirstResult(stateRow);
        query.setMaxResults(rowsPerPage);
        return query.list();
    }

    public List getSqlList(String sql) {
        return this.getSession().createSQLQuery(sql).list();
    }

    public List<T> findAll() {
        return this.getSession().createCriteria(this.entityClass).list();
    }

    public T get(Serializable id) {
        return (T) this.getSession().get(this.entityClass, id);
    }

    public T getByHql(String qlString) {
        return this.getByHql(qlString, (Parameter)null);
    }

    public T getByHql(String qlString, Parameter parameter) {
        Query query = this.createQuery(qlString, parameter);
        return (T) query.uniqueResult();
    }

    public void deleteObject(Object object) {
        this.getSession().delete(object);
    }

    public void save(T entity) {
        try {
            Object id = null;
            Method[] arr$ = entity.getClass().getMethods();
            int len$ = arr$.length;

            int i$;
            Method method;
            for(i$ = 0; i$ < len$; ++i$) {
                method = arr$[i$];
                Id idAnn = (Id)method.getAnnotation(Id.class);
                if (idAnn != null) {
                    id = method.invoke(entity);
                    break;
                }
            }

            if (StringUtils.isBlank(String.valueOf(id))) {
                arr$ = entity.getClass().getMethods();
                len$ = arr$.length;

                for(i$ = 0; i$ < len$; ++i$) {
                    method = arr$[i$];
                    PrePersist pp = (PrePersist)method.getAnnotation(PrePersist.class);
                    if (pp != null) {
                        method.invoke(entity);
                        break;
                    }
                }
            } else {
                arr$ = entity.getClass().getMethods();
                len$ = arr$.length;

                for(i$ = 0; i$ < len$; ++i$) {
                    method = arr$[i$];
                    PreUpdate pu = (PreUpdate)method.getAnnotation(PreUpdate.class);
                    if (pu != null) {
                        method.invoke(entity);
                        break;
                    }
                }
            }
        } catch (IllegalArgumentException var8) {
            var8.printStackTrace();
        } catch (IllegalAccessException var9) {
            var9.printStackTrace();
        } catch (InvocationTargetException var10) {
            var10.printStackTrace();
        }

        this.getSession().saveOrUpdate(entity);
    }

    public void update(T entity) {
        this.getSession().saveOrUpdate(entity);
    }

    public void save(List<T> entityList) {
        Iterator i$ = entityList.iterator();

        while(i$.hasNext()) {
            T entity = (T) i$.next();
            this.save(entity);
        }

    }

    public int update(String qlString) {
        return this.update(qlString, (Parameter)null);
    }

    public int update(String qlString, Parameter parameter) {
        return this.createQuery(qlString, parameter).executeUpdate();
    }

    public int deleteById(Serializable id) {
        return this.update("update " + this.entityClass.getSimpleName() + " set delFlag='" + "1" + "' where id = :p1", new Parameter(new Object[]{id}));
    }

    public int deleteById(Serializable id, String likeParentIds) {
        return this.update("update " + this.entityClass.getSimpleName() + " set delFlag = '" + "1" + "' where id = :p1 or parentIds like :p2", new Parameter(new Object[]{id, likeParentIds}));
    }

    public int updateDelFlag(Serializable id, String delFlag) {
        return this.update("update " + this.entityClass.getSimpleName() + " set delFlag = :p2 where id = :p1", new Parameter(new Object[]{id, delFlag}));
    }

    public Query createQuery(String qlString, Parameter parameter) {
        Query query = this.getSession().createQuery(qlString);
        this.setParameter(query, parameter);
        return query;
    }

    public <E> Page<E> findBySql(Page<E> page, String sqlString) {
        return this.findBySql(page, sqlString, (Parameter)null, (Class)null);
    }

    public <E> Page<E> findBySql(Page<E> page, String sqlString, Parameter parameter) {
        return this.findBySql(page, sqlString, parameter, (Class)null);
    }

    public <E> Page<E> findBySql(Page<E> page, String sqlString, Class<?> resultClass) {
        return this.findBySql(page, sqlString, (Parameter)null, resultClass);
    }

    public <E> Page<E> findBySql(Page<E> page, String sqlString, Parameter parameter, Class<?> resultClass) {
        String sql;
        SQLQuery query;
        if (!page.isDisabled() && !page.isNotCount()) {
            sql = "select count(*) " + this.removeSelect(this.removeOrders(sqlString));
            query = this.createSqlQuery(sql, parameter);
            List<Object> list = query.list();
            if (list.size() > 0) {
                page.setCount(Long.valueOf(list.get(0).toString()));
            } else {
                page.setCount((long)list.size());
            }

            if (page.getCount() < 1L) {
                return page;
            }
        }

        sql = sqlString;
        if (StringUtils.isNotBlank(page.getOrderBy())) {
            sql = sqlString + " order by " + page.getOrderBy();
        }

        query = this.createSqlQuery(sql, parameter);
        if (!page.isDisabled()) {
            query.setFirstResult(page.getFirstResult());
            query.setMaxResults(page.getMaxResults());
        }

        this.setResultTransformer(query, resultClass);
        page.setList(query.list());
        return page;
    }

    public <E> Page<E> findBySql(Page<E> page, String sqlString, Parameter parameter, Class<?> resultClass, List<String> map) {
        String sql;
        SQLQuery query;
        if (!page.isDisabled() && !page.isNotCount()) {
            sql = "select count(*) " + this.removeSelect(this.removeOrders(sqlString));
            query = this.createSqlQuery(sql, parameter);
            List<Object> list = query.list();
            if (list.size() > 0) {
                page.setCount(Long.valueOf(list.get(0).toString()));
            } else {
                page.setCount((long)list.size());
            }

            if (page.getCount() < 1L) {
                return page;
            }
        }

        sql = sqlString;
        if (StringUtils.isNotBlank(page.getOrderBy())) {
            sql = sqlString + " order by " + page.getOrderBy();
        }

        query = this.createSqlQuery(sql, parameter);
        if (!page.isDisabled()) {
            query.setFirstResult(page.getFirstResult());
            query.setMaxResults(page.getMaxResults());
        }

        query.addEntity(resultClass);
        Iterator i$ = map.iterator();

        while(i$.hasNext()) {
            String objmap = (String)i$.next();
            query.addScalar(objmap);
        }

        page.setList(query.list());
        return page;
    }

    public int getTotal(String hql) {
        List result = this.getSession().createQuery(hql).list();
        return null != result && !result.isEmpty() ? Integer.parseInt(result.get(0).toString()) : 0;
    }

    public <E> List<E> findBySql(String sqlString) {
        return this.findBySql((String)sqlString, (Parameter)null, (Class)null);
    }

    public <E> List<E> findBySql(String sqlString, Parameter parameter) {
        return this.findBySql((String)sqlString, (Parameter)parameter, (Class)null);
    }

    public <E> List<E> findBySql(String sqlString, Parameter parameter, Class<?> resultClass) {
        SQLQuery query = this.createSqlQuery(sqlString, parameter);
        this.setResultTransformer(query, resultClass);
        return query.list();
    }

    public int updateBySql(String sqlString, Parameter parameter) {
        return this.createSqlQuery(sqlString, parameter).executeUpdate();
    }

    public SQLQuery createSqlQuery(String sqlString, Parameter parameter) {
        SQLQuery query = this.getSession().createSQLQuery(sqlString);
        this.setParameter(query, parameter);
        return query;
    }

    private void setResultTransformer(SQLQuery query, Class<?> resultClass) {
        if (resultClass != null) {
            if (resultClass == Map.class) {
                query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            } else if (resultClass == List.class) {
                query.setResultTransformer(Transformers.TO_LIST);
            } else {
                query.addEntity(resultClass);
            }
        }

    }

    private void setParameter(Query query, Parameter parameter) {
        if (parameter != null) {
            Set<String> keySet = parameter.keySet();
            Iterator i$ = keySet.iterator();

            while(i$.hasNext()) {
                String string = (String)i$.next();
                Object value = parameter.get(string);
                if (value instanceof Collection) {
                    query.setParameterList(string, (Collection)value);
                } else if (value instanceof Object[]) {
                    query.setParameterList(string, (Object[])((Object[])value));
                } else {
                    query.setParameter(string, value);
                }
            }
        }

    }

    private String removeSelect(String qlString) {
        int beginPos = qlString.toLowerCase().indexOf("from");
        return qlString.substring(beginPos);
    }

    private String removeOrders(String qlString) {
        Pattern p = compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
        Matcher m = p.matcher(qlString);
        StringBuffer sb = new StringBuffer();

        while(m.find()) {
            m.appendReplacement(sb, "");
        }

        m.appendTail(sb);
        return sb.toString();
    }

    public Page<T> find(Page<T> page) {
        return this.find(page, this.createDetachedCriteria());
    }

    public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
        return this.find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
    }

    public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
        if (!page.isDisabled() && !page.isNotCount()) {
            page.setCount(this.count(detachedCriteria));
            if (page.getCount() < 1L) {
                return page;
            }
        }

        Criteria criteria = detachedCriteria.getExecutableCriteria(this.getSession());
        criteria.setResultTransformer(resultTransformer);
        if (!page.isDisabled()) {
            criteria.setFirstResult(page.getFirstResult());
            criteria.setMaxResults(page.getMaxResults());
        }

        if (StringUtils.isNotBlank(page.getOrderBy())) {
            String[] arr$ = StringUtils.split(page.getOrderBy(), ",");
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String order = arr$[i$];
                String[] o = StringUtils.split(order, " ");
                if (o.length == 1) {
                    criteria.addOrder(Order.asc(o[0]));
                } else if (o.length == 2) {
                    if ("DESC".equals(o[1].toUpperCase())) {
                        criteria.addOrder(Order.desc(o[0]));
                    } else {
                        criteria.addOrder(Order.asc(o[0]));
                    }
                }
            }
        }

        page.setList(criteria.list());
        return page;
    }

    public List<T> find(DetachedCriteria detachedCriteria) {
        return this.find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
    }

    public List<T> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(this.getSession());
        criteria.setResultTransformer(resultTransformer);
        return criteria.list();
    }

    public long count(DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(this.getSession());
        long totalCount = 0L;

        try {
            Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
            field.setAccessible(true);
            List orderEntrys = (List)field.get(criteria);
            field.set(criteria, new ArrayList());
            criteria.setProjection(Projections.rowCount());
            totalCount = Long.valueOf(criteria.uniqueResult().toString());
            criteria.setProjection((Projection)null);
            field.set(criteria, orderEntrys);
        } catch (NoSuchFieldException var7) {
            var7.printStackTrace();
        } catch (IllegalAccessException var8) {
            var8.printStackTrace();
        }

        return totalCount;
    }

    public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
        DetachedCriteria dc = DetachedCriteria.forClass(this.entityClass);
        Criterion[] arr$ = criterions;
        int len$ = criterions.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Criterion c = arr$[i$];
            dc.add(c);
        }

        return dc;
    }

}
