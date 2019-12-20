package com.barry.mockito.utis;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * 参数匹配
 * */
public class ParameterMatchTest {

    @Test
    public void with_arguments(){
        Comparable comparable = mock(Comparable.class);
        //预设根据不同的参数返回不同的结果
        when(comparable.compareTo("Test")).thenReturn(1);
        when(comparable.compareTo("Omg")).thenReturn(2);
        assertEquals(1, comparable.compareTo("Test"));
        assertEquals(2, comparable.compareTo("Omg"));
        //对于没有预设的情况会返回默认值
        assertEquals(0, comparable.compareTo("Not stub"));
    }

    /**
     * 除了匹配制定参数外，还可以匹配自己想要的任意参数
     * */
//    private class IsValid implements ArgumentMatcher<Object> {
//        @Override
//        public boolean matches(Object o) {
//            return o == 1 || o ==2;
//        }
//    }
    private class IsValid implements ArgumentMatcher<List> {
        @Override
        public boolean matches(List list) {
            return list.contains(1) || list.contains(2);
        }
    }

    @Test
    public void with_unspecified_arguments(){
        List list = mock(List.class);
        //匹配任意参数
        when(list.get(anyInt())).thenReturn(1);
        when(list.contains(argThat(new IsValid()))).thenReturn(true);

        assertEquals(1, list.get(1));
        assertEquals(1, list.get(999));
        //assertTrue(list.contains(1));
        //assertTrue(!list.contains(3));

    }






}
