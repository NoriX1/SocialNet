package dao;

import commands.Receiver;
import model.Message;
import model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public interface DataDao {
    void createTableIfNotExist();
    void getUsersFromBD();
}
