package dao;
import model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface DataDao {
    void createTableIfNotExist();
    List<User> getUsersFromBD();
}
