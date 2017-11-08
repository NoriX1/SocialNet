import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.Menu;

import java.io.IOException;


public class Starter {

    public static void main(String[] args){
        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext("config");
        Menu menu = (Menu) annotationConfigApplicationContext.getBean("menu");
        menu.showMenu();
    }
}
