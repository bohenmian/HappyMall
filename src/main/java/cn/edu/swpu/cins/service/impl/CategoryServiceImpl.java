package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.CategoryMapper;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Category;
import cn.edu.swpu.cins.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public HttpResult addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return HttpResult.createByErrorMessage("Wrong paramter");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return HttpResult.createBySuccess("Add category success");
        }
        return HttpResult.createByErrorMessage("Add category fail");
    }

    public HttpResult updateCategoryName(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return HttpResult.createByErrorMessage("Wrong paramter");
        }
        Category category = new Category();
        category.setId(parentId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return HttpResult.createBySuccess("Update category name success");
        }
        return HttpResult.createByErrorMessage("Update category name fail");
    }
}
