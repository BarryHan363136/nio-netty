package com.barry.mockito.utis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * https://www.cnblogs.com/Ming8006/p/6297333.html#top
 * */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ParamAssertTest {

    @Mock
    private List mockList;

    @Test
    public void capturing_args(){
        PersonDao personDao = mock(PersonDao.class);
        PersonService personService = new PersonService(personDao);

        ArgumentCaptor<Person> argumentCaptor = ArgumentCaptor.forClass(Person.class);
        personService.update(1, "jack");
        verify(personDao).update(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue().getId());
        assertEquals("jack", argumentCaptor.getValue().getName());
    }

    class Person{
        private int id;
        private String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    interface PersonDao{
        public void update(Person person);
    }

    class PersonService{
        private PersonDao personDao;

        PersonService(PersonDao personDao) {
            this.personDao = personDao;
        }

        public void update(int id,String name){
            personDao.update(new Person(id,name));
        }
    }

    @Test
    public void answerTest(){
        when(mockList.get(anyInt())).thenAnswer(new CustomAnswer());
        assertEquals("hello world:0",mockList.get(0));
        assertEquals("hello world:999",mockList.get(999));
    }

    private class CustomAnswer implements Answer<String>{
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return "hello world:"+args[0];
        }
    }

    @Test
    public void answer_with_callback(){
        //使用Answer来生成我们我们期望的返回
        when(mockList.get(anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return "hello world:"+args[0];
            }
        });
        assertEquals("hello world:0",mockList.get(0));
        assertEquals("hello world:999",mockList.get(999));
    }

    /**
     * 参数匹配器 matchers
     * */
    public void paraMatchers(){
        //stubbing using built-in anyInt() argument matcher
        //使用内置的anyInt()参数匹配器
        when(mockList.get(anyInt())).thenReturn("element");

        //stubbing using custom matcher
        //使用自定义的参数匹配器(在isVaild函数中返回你自己的匹配)
        //when(mockedList.contains(argThat(isValid()))).thenReturn("element");

        log.info(""+mockList.get(999));
        //也可以验证参数匹配器
        verify(mockList).get(anyInt());
    }









}
