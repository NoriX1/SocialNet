package dao.impl;

import dao.MessageDao;
import dao.UserDao;
import model.Message;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class MessageDaoJpaImpl implements MessageDao {
    private static org.apache.logging.log4j.Logger LOG = LogManager.getLogger();
    UserDao userDao = new UserDaoImpl();

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Message> getPublicMessagesFromBD() {
        List<Message> messageList =  entityManager.createQuery("from Message WHERE isprivate = :isprivate", Message.class)
                .setParameter("isprivate", 0)
                .getResultList();
        LOG.info("Receive Public MessageList: {}", messageList);
        for(Message i : messageList){
            i.setOwner(userDao.findUserById(i.getOwnerid()));
        }
        return messageList;
    }

    @Override
    public List<Message> getPrivateMessagesFromBD(int id) {
        List<Message> messageList = entityManager.createQuery("from Message WHERE target = :usertarget AND isprivate = :private", Message.class)
                .setParameter("usertarget", id)
                .setParameter("private",1)
                .getResultList();
        LOG.info("Receive Private MessageList: {}", messageList);
        for(Message i : messageList){
            i.setOwner(userDao.findUserById(i.getOwnerid()));
            i.setTarget(userDao.findUserById(i.getTargetid()));
        }
        return messageList;
    }

    @Override
    public void savePublicMessageToBD(Message message) {
        message.setId(countMessagesInDB());
        message.setOwnerid(message.getOwner().getId());
        entityManager.persist(message);
    }

    @Override
    public void savePrivateMessageToBD(Message message) {
        message.setId(countMessagesInDB());
        message.setOwnerid(message.getOwner().getId());
        message.setTargetid(message.getTarget().getId());
        entityManager.persist(message);
    }


    @Override
    public int countMessagesInDB() {
        Query query = entityManager.createQuery("SELECT COUNT(1) FROM Message");
        return Integer.parseInt(query.getSingleResult().toString());
    }
}
