package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.ShippingMapper;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.Shipping;
import cn.edu.swpu.cins.service.ShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShippingServiceImplTest {

    @Mock
    private ShippingMapper shippingMapper;

    private ShippingService shippingService;

    @Before
    public void setUp() throws Exception {
        shippingService = new ShippingServiceImpl(shippingMapper);
    }

    @Test
    public void test_addAddress_success() throws Exception {
        Shipping shipping = mock(Shipping.class);
        when(shipping.getId()).thenReturn(1);
        when(shipping.getUserId()).thenReturn(2);
        when(shipping.getReceiverName()).thenReturn("123");
        when(shipping.getReceiverPhone()).thenReturn("13541552447");
        when(shipping.getReceiverMobile()).thenReturn((long) 1234);
        when(shipping.getReceiverProvince()).thenReturn("北京");
        when(shipping.getReceiverCity()).thenReturn("北京");
        when(shipping.getReceiverDistrict()).thenReturn("1");
        when(shipping.getReceiverAddress()).thenReturn("1");
        when(shipping.getReceiverZip()).thenReturn(638000);
        shipping.setUserId(shipping.getUserId());
        when(shippingMapper.insert(shipping)).thenReturn(1);
        HttpResult result = shippingService.addAddress(shipping.getUserId(), shipping);
        assertThat(result.getStatus(), is(0));
        verify(shippingMapper).insert(shipping);
    }

    @Test
    public void test_deleteAddress_success() throws Exception {
        Integer userId = 2;
        Integer shippingId = 1;
        when(shippingMapper.deleteByShippingIdUserId(userId, shippingId)).thenReturn(1);
        HttpResult result = shippingService.deleteAddress(userId, shippingId);
        assertThat(result.getStatus(), is(0));
        verify(shippingMapper).deleteByShippingIdUserId(userId, shippingId);
    }

    @Test
    public void test_updateAddress_success() throws Exception {
        Shipping shipping = mock(Shipping.class);
        when(shipping.getId()).thenReturn(1);
        when(shipping.getUserId()).thenReturn(2);
        when(shipping.getReceiverName()).thenReturn("123");
        when(shipping.getReceiverPhone()).thenReturn("13541552447");
        when(shipping.getReceiverMobile()).thenReturn((long) 1234);
        when(shipping.getReceiverProvince()).thenReturn("北京");
        when(shipping.getReceiverCity()).thenReturn("北京");
        when(shipping.getReceiverDistrict()).thenReturn("1");
        when(shipping.getReceiverAddress()).thenReturn("1");
        when(shipping.getReceiverZip()).thenReturn(638000);
        when(shippingMapper.updateByShipping(shipping)).thenReturn(1);
        HttpResult result = shippingService.updateAddress(shipping.getUserId(), shipping);
        assertThat(result.getStatus(), is(0));
        verify(shippingMapper).updateByShipping(shipping);
    }

    @Test
    public void selectAddress() throws Exception {
        Shipping shipping = mock(Shipping.class);
        when(shipping.getId()).thenReturn(1);
        when(shipping.getUserId()).thenReturn(2);
        when(shipping.getReceiverName()).thenReturn("123");
        when(shipping.getReceiverPhone()).thenReturn("13541552447");
        when(shipping.getReceiverMobile()).thenReturn((long) 1234);
        when(shipping.getReceiverProvince()).thenReturn("北京");
        when(shipping.getReceiverCity()).thenReturn("北京");
        when(shipping.getReceiverDistrict()).thenReturn("1");
        when(shipping.getReceiverAddress()).thenReturn("1");
        when(shipping.getReceiverZip()).thenReturn(638000);
        when(shippingMapper.selectByShippingIdUserId(shipping.getUserId(), shipping.getId())).thenReturn(shipping);
        HttpResult result = shippingService.selectAddress(shipping.getUserId(), shipping.getId());
        assertThat(result.getStatus(), is(0));
        verify(shippingMapper).selectByShippingIdUserId(shipping.getUserId(), shipping.getId());
    }

    @Test
    public void getList() throws Exception {
        int pageNum = 1;
        int pageSize = 10;
        Integer userId = 2;
        Shipping shipping = mock(Shipping.class);
        when(shipping.getId()).thenReturn(1);
        when(shipping.getUserId()).thenReturn(2);
        when(shipping.getReceiverName()).thenReturn("123");
        when(shipping.getReceiverPhone()).thenReturn("13541552447");
        when(shipping.getReceiverMobile()).thenReturn((long) 1234);
        when(shipping.getReceiverProvince()).thenReturn("北京");
        when(shipping.getReceiverCity()).thenReturn("北京");
        when(shipping.getReceiverDistrict()).thenReturn("1");
        when(shipping.getReceiverAddress()).thenReturn("1");
        when(shipping.getReceiverZip()).thenReturn(638000);
        PageHelper.startPage(1, 10);
        List<Shipping> shippingList = singletonList(shipping);
        when(shippingMapper.selectByUserId(userId)).thenReturn(shippingList);
        HttpResult httpResult = shippingService.getList(userId, pageNum, pageSize);
        assertThat(httpResult.getStatus(), is(0));
        verify(shippingMapper).selectByUserId(userId);
    }

}