package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.CartMapper;
import cn.edu.swpu.cins.dao.ProductMapper;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.dto.view.CartVo;
import cn.edu.swpu.cins.entity.Cart;
import cn.edu.swpu.cins.entity.Product;
import cn.edu.swpu.cins.service.CartService;
import com.google.common.base.Splitter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ProductMapper productMapper;

    private CartService cartService;
    @Before
    public void setUp() throws Exception {
        cartService = new CartServiceImpl(cartMapper, productMapper);
    }

    @Test
    public void test_when_cart_exit_add_success() throws Exception {
        Integer userId = 1;
        Integer productId = 2;
        Integer count = 5;
        Cart cart = mock(Cart.class);
        when(cart.getId()).thenReturn(1);
        when(cart.getUserId()).thenReturn(1);
        when(cart.getQuantity()).thenReturn(1);
        when(cart.getProductId()).thenReturn(2);
        when(cartMapper.selectCartByUserIdAndProductId(userId, productId)).thenReturn(cart);
        HttpResult result = cartService.add(userId, productId, count);
        assertThat(result.getStatus(), is(0));
        verify(cartMapper).selectCartByUserIdAndProductId(userId, productId);
    }

    @Test
    public void test_when_cart_not_exit_add_success() throws Exception {
        Integer userId = 1;
        Integer productId = 2;
        Integer count = 5;
        when(cartMapper.selectCartByUserIdAndProductId(userId, productId)).thenReturn(null);
        HttpResult result = cartService.add(userId, productId, count);
        assertThat(result.getStatus(), is(0));
        verify(cartMapper).selectCartByUserIdAndProductId(userId, productId);
    }

    @Test
    public void test_updateCart_success() throws Exception {
        Integer userId = 1;
        Integer productId = 2;
        Integer count = 5;
        Cart cart = mock(Cart.class);
        when(cart.getId()).thenReturn(1);
        when(cart.getUserId()).thenReturn(1);
        when(cartMapper.selectCartByUserIdAndProductId(userId, productId)).thenReturn(cart);
        when(cartMapper.updateByPrimaryKeySelective(cart)).thenReturn(1);
        HttpResult result = cartService.updateCart(userId, productId, count);
        assertThat(result.getStatus(), is(0));
        verify(cartMapper).selectCartByUserIdAndProductId(userId, productId);
        verify(cartMapper).updateByPrimaryKeySelective(cart);
    }

    @Test
    public void deleteProduct() throws Exception {
        Integer userId = 1;
        String productIds = "123";
        List<String> productList = Splitter.on(",").splitToList(productIds);
        when(cartMapper.deleteByUserIdAndProductId(userId, productList)).thenReturn(3);
        HttpResult result = cartService.deleteProduct(userId, productIds);
        assertThat(result.getStatus(), is(0));
        verify(cartMapper).deleteByUserIdAndProductId(userId, productList);
    }

    @Test
    public void getList() throws Exception {
        Integer userId = 1;
        HttpResult result = cartService.getList(userId);
        assertThat(result.getStatus(), is(0));
    }

    @Test
    public void selectAll() throws Exception {
        Integer userId = 1;
        Integer productId = 2;
        Integer checked = 1;
        when(cartMapper.checkedOrUnCheckedProduct(userId, productId, checked)).thenReturn(1);
        HttpResult result = cartService.selectAll(userId, productId, checked);
        assertThat(result.getStatus(), is(0));
        verify(cartMapper).checkedOrUnCheckedProduct(userId, productId, checked);
    }

    @Test
    public void getProductCount() throws Exception {
        Integer userId = 1;
        HttpResult result = cartService.getList(userId);
        assertThat(result.getStatus(), is(0));
    }

}