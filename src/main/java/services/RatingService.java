package services;

import model.Message;

import java.util.*;

public class RatingService {
    public Map<String, Integer> getWordTop(List<Message> messages){
        Map<String,Integer> wordmap = new HashMap<>();
        Map<String,Integer> top = new HashMap<>();
        int count = 0;
        for(Message mes : messages){
            String words[] = mes.getMessage().toLowerCase().replaceAll("\\p{Punct}", "").split(" ");
            for(String word : words){
                for (int i = 0; i < words.length; i++) {
                    if (word == words[i]){
                        count++;
                    }
                }
                if (wordmap.containsKey(word))
                {
                    wordmap.put(word, wordmap.get(word)+count);
                }
                else
                {
                    wordmap.put(word, count);
                }
                count = 0;
            }
        }
        if(wordmap.containsKey(""))
            wordmap.remove("");
        if(wordmap.containsKey(" "))
            wordmap.remove(" ");
        List list = new ArrayList(wordmap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });

        while (list.size() > 10)
        {
            list.remove(list.size()-1);
        }

        for (int i = 0; i < list.size(); i++) {
            String elem[] = list.get(i).toString().split("=");
            top.put(elem[0], list.size() - i);
        }

        return top;
    }

    public List<String> getRating(List<Message> messages, Map<String, Integer> wordTop){
        Map<String, Integer> rating = new HashMap<>();
        int points = 0;
        for(Message mes : messages){
            String words[] = mes.getMessage().toLowerCase().replaceAll("\\p{Punct}", "").split(" ");
            for(String word : words){
                if(wordTop.containsKey(word)){
                    points += wordTop.get(word);
                }
            }
            rating.put("id="+mes.getOwner().getId()+" "+mes.getOwner().getName()+" "+
                    mes.getOwner().getSurname()+": "+mes.getMessage(),points);
            points = 0;
        }

        List list = new ArrayList(rating.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });

        List<String> formattedList = new LinkedList<>();
        for(Object element : list){
            StringBuilder str = new StringBuilder();
            int index = element.toString().lastIndexOf("=");
            str.insert(0,element.toString());
            str.insert(index+1," ");
            str.insert(index," (Rating ");
            str.append(")");
            formattedList.add(str.toString());
        }
        return formattedList;
    }
}
