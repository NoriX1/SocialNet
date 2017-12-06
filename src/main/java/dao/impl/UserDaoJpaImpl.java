package dao.impl;

import dao.UserDao;
import model.Friend;
import model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

@Repository
@Transactional
public class UserDaoJpaImpl implements UserDao{
    private static org.apache.logging.log4j.Logger LOG = LogManager.getLogger();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public String userToString(User user) {
        return new String( "id="+user.getId()+",Name: "+user.getName()+",Surname: "+
                user.getSurname()+",Age: "+user.getAge()+",Sex: "+user.getSex());
    }

    @Override
    public int countUserInDB() {
        Query query = entityManager.createQuery("SELECT COUNT(1) FROM User");
        return Integer.parseInt(query.getSingleResult().toString());

    }

    @Override
    public Boolean checkLoginForExisting(User checkuser) {
        try{User user = entityManager.createQuery("FROM User u WHERE login = :login", User.class)
                .setParameter("login",checkuser.getLogin()).getSingleResult();
        LOG.info("Finded user {}", user);
        LOG.info("With login {}", user.getLogin());}
        catch (NoResultException e){
            LOG.info("All right, this login is not found ",e);
            return true;
        }
        LOG.info("ERROR: Login {} is exist", checkuser.getLogin());
        return false;
    }

    @Override
    public List<User> getUserListFromNetwork() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public String loggingIn(String login, String password) {
        LOG.info("Logging in: ");
        boolean finded = false;
        LOG.info("Getting UserList From Network");
        List<User> userList = getUserListFromNetwork();
        LOG.info("Received UserList {}", userList);
        for(User i : userList){
            if (i.getLogin().toLowerCase().equals(login.toLowerCase())){
                finded = true;
                if(i.getPassword().equals(password)){
                    LOG.info("User {} is logged", i);
                    return Integer.toString(i.getId()) ;
                }
                else{
                    LOG.info("Incorrect password");
                    return "null";

                }
            }
        }
        if (!finded) {
            LOG.info("Login does not exist!");
            return "null";
        }
        finded = false;
        return "null";
    }


    @Override
    public void saveFriendToFriendlistBD(int who, int whom) {
        Friend friend = new Friend(countFriendsInDB(), who, whom);
        entityManager.persist(friend);
    }

    @Override
    public List<User> loadFriendListFromBD(int userID) {
        List<User> userList = new LinkedList<>();
        List<Friend> userIDList = entityManager.createQuery("FROM Friend WHERE who = :id", Friend.class)
                .setParameter("id", userID)
                .getResultList();
        LOG.info("Receive list of ID {}", userIDList);
        for(Friend i : userIDList){
            userList.add(findUserById(i.getWhom()));
        }
        return userList;
    }

    @Override
    public int countFriendsInDB() {
        Query query = entityManager.createQuery("SELECT COUNT(1) FROM Friend");
        return Integer.parseInt(query.getSingleResult().toString());
    }
}
