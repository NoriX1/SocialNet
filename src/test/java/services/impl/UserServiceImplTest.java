package services.impl;

import dao.DataDao;
import dao.impl.DataDaoImpl;
import model.Message;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import services.UserService;
import services.exceptions.NoUserFoundInDBException;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private UserService userService;
    @Mock
    private DataDao dataDao;

    @Before
    public void setUp(){
        userService = new UserServiceImpl(dataDao);
        when(dataDao.findUserInBD(anyInt())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user1 = new User(0,"Дмитрий","Никоненко",22,"Мужской",
                        "NoriX","123456");
                User user2 = new User(1,"Willy","Adams",23,"Мужской",
                        "second","second");
                int id = invocationOnMock.getArgument(0);
                if (id == 0){
                    return user1;
                }
                if (id == 1){
                    return user2;
                }
                throw (new NoUserFoundInDBException("User with id = "+id+" not found!"));
            }
        });
        when(dataDao.getPrivateMessagesFromBD(anyInt())).thenAnswer((Answer<List<Message>>) invocation ->{
            int id = invocation.getArgument(0);
            User user = dataDao.findUserInBD(id);
            User target = new User(3, "Fill");
            List<Message> messages = new LinkedList<>();
            messages.add(new Message(user, "Hello!",target,true));
            messages.add(new Message(user, "World!",target,true));
            return messages;
        });
    }

    @Test
    public void testgetUserById(){
        User expecteduser1 = new User(0,"Дмитрий","Никоненко",22,"Мужской",
                "NoriX","123456");
        User expecteduser2 = new User(1,"Willy","Adams",23,"Мужской",
                "second","second");
        assertEquals(expecteduser1, userService.getUserById(0));
        assertEquals(expecteduser2, userService.getUserById(1));
    }

    @Test(expected = NoUserFoundInDBException.class)
    public void testNoUserFoundInDBException(){
        userService.getUserById(5);
    }

    @Test
    public void testgetListOfPrivateMessages(){
        List<Message> expectedmessages = new LinkedList<>();
        User target = new User(3, "Fill");
        int id = 1;
        expectedmessages.add(new Message(userService.getUserById(id), "Hello!",target,true));
        expectedmessages.add(new Message(userService.getUserById(id), "World!",target,true));
        assertEquals(expectedmessages, userService.getListOfPrivateMessages(id));
    }

    @Test
    public void testgetNameOfUserById() {
        String name = "Willy";
        Integer id = 1;
        assertEquals(name, userService.getNameOfUserById(id));
        ArgumentCaptor<Integer> idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(dataDao).findUserInBD(idArgumentCaptor.capture());
        assertEquals(id, idArgumentCaptor.getValue());
    }

    @Test
    public void testChangeNameOfUser(){
        User expecteduser1 = new User(1,"Tom","Adams",23,"Мужской",
                "second","second");
        String newname = "Tom";
        String badname = "      ";
        Integer id = 1;
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userService.changeNameOfUser(id, newname);
        verify(dataDao).saveUser(userArgumentCaptor.capture());
        assertEquals(expecteduser1, userArgumentCaptor.getValue());
        //#2
        userService.changeNameOfUser(id, badname);
        verify(dataDao, times(1)).saveUser(expecteduser1);

    }


}