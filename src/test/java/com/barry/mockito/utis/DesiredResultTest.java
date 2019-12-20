package com.barry.mockito.utis;

import com.barry.mockito.domain.Account;
import com.barry.mockito.domain.RailwayTicket;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@Slf4j
public class DesiredResultTest {

    @Test
    public void when_thenReturn(){
        //mock一个Iterator类
        Iterator interator = mock(Iterator.class);
        //预设interator调用next()时第一返回hello,第n次都返回world
        when(interator.next()).thenReturn("hello").thenReturn("world");
        //使用mock的对象
        String result = interator.next() + " " + interator.next() + " " + interator.next();
        //验证结果
        assertEquals("hello world world", result);
    }

    @Test(expected = IOException.class)
    public void when_thenThrow() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        //预设当流关闭时抛出异常
        doThrow(new IOException()).when(outputStream).close();
        outputStream.close();
    }

    /**
     * 在创建mock对象时，有的方法我们没有进行stubbing，所以调用时会放回Null这样在进行操作是很可能抛出NullPointerException。
     * 如果通过RETURNS_SMART_NULLS参数创建的mock对象在没有调用stubbed方法时会返回SmartNull。
     * 例如：返回类型是String，会返回"";是int，会返回0；是List，会返回空的List。
     * 另外，在控制台窗口中可以看到SmartNull的友好提示。
     * */
    @Test
    public void returnSmartNullsTest(){
        List mock = mock(List.class, RETURNS_SMART_NULLS);
        log.info("=========>"+mock.get(0));
        //使用RETURNS_SMART_NULLS参数创建的mock对象,不会抛出NullPointerException异常，
        //另外控制台窗口会提示信息"SmartNull returned by unstubbed get() method on mock"
        log.info("===========>"+mock.toArray().length);
    }

    /**
     * RETURNS_DEEP_STUBS也是创建mock对象时的备选参数
     * RETURNS_DEEP_STUBS参数程序会自动进行mock所需的对象，
     * 方法deepstubsTest和deepstubsTest2是等价的
     * */
    @Test
    public void deepStubsTest(){
        Account account = mock(Account.class, RETURNS_DEEP_STUBS);
        when(account.getRailwayTicket().getDestination()).thenReturn("Beijing");
        account.getRailwayTicket().getDestination();
        verify(account.getRailwayTicket()).getDestination();
        assertEquals("Beijing", account.getRailwayTicket().getDestination());
    }

    @Test
    public void deepStubTest2(){
        Account account = mock(Account.class);
        RailwayTicket railwayTicket = mock(RailwayTicket.class);
        when(account.getRailwayTicket()).thenReturn(railwayTicket);
        when(railwayTicket.getDestination()).thenReturn("Beijing");

        account.getRailwayTicket().getDestination();
        verify(account.getRailwayTicket()).getDestination();
        assertEquals("Beijing", account.getRailwayTicket().getDestination());
    }

    /**
     * 模拟方法抛出异常
     * */
    @Test(expected = RuntimeException.class)
    public void doThrow_when(){
        List list = mock(List.class);
        doThrow(new RuntimeException()).when(list).add(1);

        list.add(1);
    }






}
