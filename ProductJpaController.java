/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.jpa.project.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sit.jpa.project.model.Category;
import sit.jpa.project.model.History;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import sit.jpa.project.controller.exceptions.NonexistentEntityException;
import sit.jpa.project.controller.exceptions.PreexistingEntityException;
import sit.jpa.project.controller.exceptions.RollbackFailureException;
import sit.jpa.project.model.Product;

/**
 *
 * @author Chonticha Sae-jiw
 */
public class ProductJpaController implements Serializable {

    public ProductJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Product product) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (product.getHistoryList() == null) {
            product.setHistoryList(new ArrayList<History>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Category categoryId = product.getCategoryId();
            if (categoryId != null) {
                categoryId = em.getReference(categoryId.getClass(), categoryId.getCategoryId());
                product.setCategoryId(categoryId);
            }
            List<History> attachedHistoryList = new ArrayList<History>();
            for (History historyListHistoryToAttach : product.getHistoryList()) {
                historyListHistoryToAttach = em.getReference(historyListHistoryToAttach.getClass(), historyListHistoryToAttach.getHistoryId());
                attachedHistoryList.add(historyListHistoryToAttach);
            }
            product.setHistoryList(attachedHistoryList);
            em.persist(product);
            if (categoryId != null) {
                categoryId.getProductList().add(product);
                categoryId = em.merge(categoryId);
            }
            for (History historyListHistory : product.getHistoryList()) {
                Product oldProductIdOfHistoryListHistory = historyListHistory.getProductId();
                historyListHistory.setProductId(product);
                historyListHistory = em.merge(historyListHistory);
                if (oldProductIdOfHistoryListHistory != null) {
                    oldProductIdOfHistoryListHistory.getHistoryList().remove(historyListHistory);
                    oldProductIdOfHistoryListHistory = em.merge(oldProductIdOfHistoryListHistory);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProduct(product.getProductId()) != null) {
                throw new PreexistingEntityException("Product " + product + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Product product) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Product persistentProduct = em.find(Product.class, product.getProductId());
            Category categoryIdOld = persistentProduct.getCategoryId();
            Category categoryIdNew = product.getCategoryId();
            List<History> historyListOld = persistentProduct.getHistoryList();
            List<History> historyListNew = product.getHistoryList();
            if (categoryIdNew != null) {
                categoryIdNew = em.getReference(categoryIdNew.getClass(), categoryIdNew.getCategoryId());
                product.setCategoryId(categoryIdNew);
            }
            List<History> attachedHistoryListNew = new ArrayList<History>();
            for (History historyListNewHistoryToAttach : historyListNew) {
                historyListNewHistoryToAttach = em.getReference(historyListNewHistoryToAttach.getClass(), historyListNewHistoryToAttach.getHistoryId());
                attachedHistoryListNew.add(historyListNewHistoryToAttach);
            }
            historyListNew = attachedHistoryListNew;
            product.setHistoryList(historyListNew);
            product = em.merge(product);
            if (categoryIdOld != null && !categoryIdOld.equals(categoryIdNew)) {
                categoryIdOld.getProductList().remove(product);
                categoryIdOld = em.merge(categoryIdOld);
            }
            if (categoryIdNew != null && !categoryIdNew.equals(categoryIdOld)) {
                categoryIdNew.getProductList().add(product);
                categoryIdNew = em.merge(categoryIdNew);
            }
            for (History historyListOldHistory : historyListOld) {
                if (!historyListNew.contains(historyListOldHistory)) {
                    historyListOldHistory.setProductId(null);
                    historyListOldHistory = em.merge(historyListOldHistory);
                }
            }
            for (History historyListNewHistory : historyListNew) {
                if (!historyListOld.contains(historyListNewHistory)) {
                    Product oldProductIdOfHistoryListNewHistory = historyListNewHistory.getProductId();
                    historyListNewHistory.setProductId(product);
                    historyListNewHistory = em.merge(historyListNewHistory);
                    if (oldProductIdOfHistoryListNewHistory != null && !oldProductIdOfHistoryListNewHistory.equals(product)) {
                        oldProductIdOfHistoryListNewHistory.getHistoryList().remove(historyListNewHistory);
                        oldProductIdOfHistoryListNewHistory = em.merge(oldProductIdOfHistoryListNewHistory);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = product.getProductId();
                if (findProduct(id) == null) {
                    throw new NonexistentEntityException("The product with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Product product;
            try {
                product = em.getReference(Product.class, id);
                product.getProductId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The product with id " + id + " no longer exists.", enfe);
            }
            Category categoryId = product.getCategoryId();
            if (categoryId != null) {
                categoryId.getProductList().remove(product);
                categoryId = em.merge(categoryId);
            }
            List<History> historyList = product.getHistoryList();
            for (History historyListHistory : historyList) {
                historyListHistory.setProductId(null);
                historyListHistory = em.merge(historyListHistory);
            }
            em.remove(product);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Product> findProductEntities() {
        return findProductEntities(true, -1, -1);
    }

    public List<Product> findProductEntities(int maxResults, int firstResult) {
        return findProductEntities(false, maxResults, firstResult);
    }

    private List<Product> findProductEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Product.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Product findProduct(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Product> rt = cq.from(Product.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Product> findByProductName(String productName) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Product.findByProductName");
            query.setParameter("productName", "%" + productName.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Product> Search(String productName, Category category) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Product.findByCategoryIdAndName");
            query.setParameter("productName", "%" + productName.toLowerCase() + "%");
            query.setParameter("Category_Id", category);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Product> SearchByPrice(Double price, Category category) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Product.findByPrice");
            query.setParameter("prices", price);
            query.setParameter("Category_Id", category);

            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Product> SearchAllByPrice(Double price) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Product.findAllByPrice");
            query.setParameter("prices", price);          
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Product> findAll() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Product.findAll");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

       public List<Product> findCategoryId(Category category) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Product.findCategoryId");
            query.setParameter("Category_Id", category);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
