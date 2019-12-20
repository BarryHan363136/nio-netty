package com.barry.mockito.utis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AnnotationSimulationTest2 {

    @Mock
    private List mockList;

    @Test
    public void shorthand(){
        mockList.add(1);
        verify(mockList).add(1);
    }



}
