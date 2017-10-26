package services;

import model.Message;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RatingServiceTest {
    private final Map<String, Integer> topOfWords = new HashMap<String, Integer>();
    private final List<Message> MessageList = new LinkedList<>();
    private final List<String> resultStrings = new LinkedList<>();
    private final List<Message> MessageList2 = new LinkedList<>();
    private final List<String> resultStrings2 = new LinkedList<>();

    @Before
    public void setUpData(){
        topOfWords.put("чего", 1);
        topOfWords.put("не", 2);
        topOfWords.put("на", 3);
        topOfWords.put("электронной", 4);
        topOfWords.put("с", 5);
        topOfWords.put("и", 6);
        topOfWords.put("отделении", 7);
        topOfWords.put("от", 8);
        topOfWords.put("робот", 9);
        topOfWords.put("в", 10);
        MessageList.add(new Message(new User("Name1"),"Первые роботы компании Promobot появились в московских отделениях «Сбербанка» на Ленинградском проспекте и Открытом шоссе."));
        MessageList.add(new Message(new User("Name2"),"Работа одного из ботов, который умеет записывать аудио и видео, состоит в том, чтобы собирать отзывы клиентов по результатам обслуживания."));
        MessageList.add(new Message(new User("Name1"),"Второй робот управляет электронной очередью в отделении."));
        MessageList.add(new Message(new User("Name2"),"С учетом появления в штате новых сотрудников, в «Сбербанке» разработали новую схему получения обратной связи от клиента."));
        MessageList.add(new Message(new User("Name3"),"Представители финансовой структуры уверены, что клиентам общаться с роботом будет интереснее, чем, например, заполнять анкету. Поэтому, от роботов ждут эффективной службы."));
        MessageList.add(new Message(new User("Name2"),"В другом отделении робот заменил собой терминал, выдающий талоны электронной очереди."));
        MessageList.add(new Message(new User("Name1"),"«Электронному сотруднику» можно словами объяснить цель вашего визита в банк, после чего он сам выдаст необходимый талон."));
        MessageList.add(new Message(new User("Name2"),"На случай, если робот все же не поймет, чего от него хочет клиент, он также оборудован тачскрином."));
        MessageList.add(new Message(new User("Name3"),"Хочется надеяться, что робот поможет уменьшить очередь, а не создать дополнительную."));
        MessageList.add(new Message(new User("Name2"),"В перспективе «Сбербанк» планирует передать роботам функции консультантов."));
        resultStrings.add("Name2 Undefinded: С учетом появления в штате новых сотрудников, в «Сбербанке» разработали новую схему получения обратной связи от клиента. (Rating = 33)");
        resultStrings.add("Name1 Undefinded: Второй робот управляет электронной очередью в отделении. (Rating = 30)");
        resultStrings.add("Name2 Undefinded: В другом отделении робот заменил собой терминал, выдающий талоны электронной очереди. (Rating = 30)");
        resultStrings.add("Name2 Undefinded: На случай, если робот все же не поймет, чего от него хочет клиент, он также оборудован тачскрином. (Rating = 23)");
        resultStrings.add("Name1 Undefinded: Первые роботы компании Promobot появились в московских отделениях «Сбербанка» на Ленинградском проспекте и Открытом шоссе. (Rating = 19)");
        resultStrings.add("Name2 Undefinded: Работа одного из ботов, который умеет записывать аудио и видео, состоит в том, чтобы собирать отзывы клиентов по результатам обслуживания. (Rating = 16)");
        resultStrings.add("Name3 Undefinded: Представители финансовой структуры уверены, что клиентам общаться с роботом будет интереснее, чем, например, заполнять анкету. Поэтому, от роботов ждут эффективной службы. (Rating = 13)");
        resultStrings.add("Name1 Undefinded: «Электронному сотруднику» можно словами объяснить цель вашего визита в банк, после чего он сам выдаст необходимый талон. (Rating = 11)");
        resultStrings.add("Name3 Undefinded: Хочется надеяться, что робот поможет уменьшить очередь, а не создать дополнительную. (Rating = 11)");
        resultStrings.add("Name2 Undefinded: В перспективе «Сбербанк» планирует передать роботам функции консультантов. (Rating = 10)");
        MessageList2.add(new Message(new User("Test1"), ""));
        MessageList2.add(new Message(new User("Test2"), "            "));
        MessageList2.add(new Message(new User("Test3"), "     раз   два   три"));
        MessageList2.add(new Message(new User("Test4"), "раз раз раз   два        два          три"));
        MessageList2.add(new Message(new User("Test5"), ":|/ , , . . | !! привет! () ( )) 9( пока привет"));
        MessageList2.add(new Message(new User("Test6"), "! ! ? ?  ?  ? ? ? ??? ? 7 ? ? ? ? ? test"));
        resultStrings2.add("Test4 Undefinded: раз раз раз   два        два          три (Rating = 44)");
        resultStrings2.add("Test3 Undefinded:      раз   два   три (Rating = 21)");
        resultStrings2.add("Test5 Undefinded: :|/ , , . . | !! привет! () ( )) 9( пока привет (Rating = 13)");
        resultStrings2.add("Test6 Undefinded: ! ! ? ?  ?  ? ? ? ??? ? 7 ? ? ? ? ? test (Rating = 7)");
        resultStrings2.add("Test2 Undefinded:              (Rating = 0)");
        resultStrings2.add("Test1 Undefinded:  (Rating = 0)");
    }
    @Test
    public void getWordTopTest(){
        RatingService ratingService = new RatingService();
        Map<String, Integer> result = ratingService.getWordTop(MessageList);
        for (Map.Entry<String, Integer> map : topOfWords.entrySet()){
            if(result.containsKey(map.getKey())){
                assertEquals("Error! Value is not correct!", topOfWords.get(map.getKey()), result.get(map.getKey()));
            }
        }
    }
    @Test
    public void getRatingTest(){
        RatingService ratingService = new RatingService();
        List<String> result = ratingService.getRating(MessageList, ratingService.getWordTop(MessageList));
        for (String TestList : resultStrings){
            for (String ResultList : result){
                assertEquals("Error! String is incorrect!", resultStrings, result);
            }
        }
    }
    @Test
    public void getRatingTest2(){
        RatingService ratingService = new RatingService();
        List<String> result = ratingService.getRating(MessageList2, ratingService.getWordTop(MessageList2));
        for (String TestList : resultStrings2){
            for (String ResultList : result){
                assertEquals("Error! String is incorrect!", resultStrings2, result);
            }
        }
    }
}

