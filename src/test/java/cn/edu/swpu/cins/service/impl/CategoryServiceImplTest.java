package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.CategoryMapper;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.Category;
import cn.edu.swpu.cins.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryService categoryService;

    @Before
    public void setUp() throws Exception {
        categoryService = new CategoryServiceImpl(categoryMapper);
    }

    @Test
    public void test_addCategory_success() throws Exception {
        String categoryName = "123";
        Integer parentId = 123;
        Category category = mock(Category.class);
        when(categoryMapper.insert(category)).thenReturn(1);
        HttpResult result = categoryService.addCategory(categoryName, parentId);
        assertThat(result.getStatus(), is(0));
        verify(categoryMapper).insert(category);
    }

    @Test
    public void updateCategoryName() throws Exception {
    }


    @Test
    public void getChildrenParallelCategory() throws Exception {
    }

    @Test
    public void getCategory() throws Exception {
    }

}