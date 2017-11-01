import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.Menu;

import java.io.IOException;


public class Starter {

    public static void main(String[] args){
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("application-context.xml");
        Menu menu = (Menu) applicationContext.getBean("menu");
        menu.showMenu();
    }
}
