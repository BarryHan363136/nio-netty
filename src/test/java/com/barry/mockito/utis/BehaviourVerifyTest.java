package com.barry.mockito.utis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import static org.mockito.Mockito.*;

/**
 * https://www.bilibili.com/video/av67760177?p=2
 * https://blog.csdn.net/bboyfeiyu/article/details/52127551
 * */
@Slf4j
public class BehaviourVerifyTest {

    @Test
    public void verify_behaviour(){
        //模拟创建一个Listdui
        List mock = mock(List.class);
        //使用mock的对象
        mock.add(1);
        mock.clear();
        //验证add(1)和clear()的行为是否发生
        verify(mock).add(1);
        verify(mock).clear();
    }

    /**
     * 默认情况下，所有的函数都有返回值。
     * mock函数默认返回的是null，一个空的集合或者一个被对象类型包装的内置类型，例如0、false对应的对象类型为Integer、Boolean；
     * 测试桩函数可以被覆写 : 例如常见的测试桩函数可以用于初始化夹具，但是测试函数能够覆写它。请注意，覆写测试桩函数是一种可能存在潜在问题的做法；
     * 一旦测试桩函数被调用，该函数将会一致返回固定的值；
     * 上一次调用测试桩函数有时候极为重要-当你调用一个函数很多次时，最后一次调用可能是你所感兴趣的。
     * */
    @Test/**(expected = RuntimeException.class)*/
    public void StubTesting(){
        //既可以mock接口也可以mock具体的类型
        List mockList = mock(LinkedList.class);

        //测试桩
        when(mockList.get(0)).thenReturn("first");
        when(mockList.get(1)).thenThrow(new RuntimeException());

        //following prints "first"
        //输出first
        log.info((String) mockList.get(0));

        //following throw runtime exception
        //抛出异常,如果
        //log.info(""+mockList.get(1));

        //following prints "null" because was not stubbed
        //因为get(999)没有打桩,因此输出null
        log.info(""+mockList.get(999));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns then something else breaks (often before even verify() gets executed).
        //If your code doesn't care what get(0) returns then it should not be stubbed. Not convinced? See here.
        // 验证get(0)被调用的次数 verify(mockedList).get(0);
        verify(mockList).get(0);
    }


}
