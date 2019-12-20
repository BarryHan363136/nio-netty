package com.barry.mockito.utis;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.mockito.Mockito.verify;

/**
 * 使用注解快速模拟
 * */
public class AnnotationSimulationTest {

    @Mock
    private List mockList;

    /**
     * 运行这个测试类你会发现报错了，mock的对象为NULL，为此我们必须在基类中添加初始化mock的代码
     * */
    public AnnotationSimulationTest(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shorthand(){
        mockList.add(1);
        verify(mockList).add(1);
    }


}
