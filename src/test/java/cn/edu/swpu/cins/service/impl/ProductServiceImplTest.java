package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.CategoryMapper;
import cn.edu.swpu.cins.dao.ProductMapper;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.Product;
import cn.edu.swpu.cins.service.CategoryService;
import cn.edu.swpu.cins.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;
    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryService categoryService;

    private ProductService productService;



    @Before
    public void setUp() throws Exception {
        productService = new ProductServiceImpl(productMapper, categoryMapper, categoryService);
    }

    @Test
    public void test_saveOrUpdateProduct_success() throws Exception {
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(1);
        when(product.getCategoryId()).thenReturn(2);
        when(product.getName()).thenReturn("apple");
        when(product.getSubtitle()).thenReturn("特价");
        when(product.getSubImages()).thenReturn("123.jpg");
        when(product.getDetail()).thenReturn("123");
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(12.33));
        when(product.getStock()).thenReturn(10);
        when(product.getStatus()).thenReturn(1);
        when(product.getCreateTime()).thenReturn(null);
        when(product.getUpdateTime()).thenReturn(null);
        when(productMapper.updateByPrimaryKey(product)).thenReturn(1);
        HttpResult result = productService.saveOrUpdateProduct(product);
        assertThat(result.getStatus(), is(0));
        verify(productMapper).updateByPrimaryKey(product);
    }

    @Test
    public void test_manageProductDetail_success() throws Exception {
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(2);
        when(product.getCategoryId()).thenReturn(2);
        when(product.getName()).thenReturn("apple");
        when(product.getSubtitle()).thenReturn("特价");
        when(product.getSubImages()).thenReturn("123.jpg");
        Integer productId = 2;
        when(productMapper.selectByPrimaryKey(productId)).thenReturn(product);
        HttpResult result = productService.manageProductDetail(productId);
        assertThat(result.getStatus(), is(0));
        verify(productMapper).selectByPrimaryKey(productId);
    }

    @Test
    public void test_getProductList_success() throws Exception {
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(2);
        when(product.getCategoryId()).thenReturn(2);
        when(product.getName()).thenReturn("apple");
        when(product.getSubtitle()).thenReturn("特价");
        when(product.getSubImages()).thenReturn("123.jpg");
        when(product.getDetail()).thenReturn("123");
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(12.33));
        when(product.getStock()).thenReturn(10);
        when(product.getStatus()).thenReturn(0);
        when(product.getCreateTime()).thenReturn(null);
        when(product.getUpdateTime()).thenReturn(null);
        int pageNum = 1;
        int pageSize = 10;
        List<Product> list = singletonList(product);
        when(productMapper.getProductList()).thenReturn(list);
        HttpResult result = productService.getProductList(pageNum, pageSize);
        assertThat(result.getStatus(), is(0));
        verify(productMapper).getProductList();
    }

    @Test
    public void searchProduct() throws Exception {
        String productName = "apple";
        Integer productId = 2;
        Integer pageNum = 1;
        Integer pageSize = 10;
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(2);
        when(product.getCategoryId()).thenReturn(2);
        when(product.getName()).thenReturn("apple");
        when(product.getSubtitle()).thenReturn("特价");
        when(product.getSubImages()).thenReturn("123.jpg");
        when(product.getDetail()).thenReturn("123");
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(12.33));
        when(product.getStock()).thenReturn(10);
        when(product.getStatus()).thenReturn(0);
        when(product.getCreateTime()).thenReturn(null);
        when(product.getUpdateTime()).thenReturn(null);
        productName = new StringBuilder().append("%").append("%").toString();
        List<Product> list = singletonList(product);
        when(productMapper.selectByNameAndproductId(productName, productId)).thenReturn(list);
        HttpResult result = productService.searchProduct(productName, productId, pageNum, pageSize);
        assertThat(result.getStatus(), is(0));
        verify(productMapper).selectByNameAndproductId(productName, productId);
    }

    @Test
    public void getProductDetail() throws Exception {
        Integer productId = 2;
        Product product = mock(Product.class);
        when(product.getStatus()).thenReturn(1);
        when(productMapper.selectByPrimaryKey(productId)).thenReturn(product);
        HttpResult result = productService.getProductDetail(productId);
        assertThat(result.getStatus(), is(0));
        verify(productMapper).selectByPrimaryKey(productId);
    }

}