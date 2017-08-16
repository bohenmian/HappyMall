package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Category;

import java.util.List;

public interface CategoryService {

    HttpResult addCategory(String categoryName, Integer parentId);

    HttpResult updateCategoryName(String categoryName, Integer parentId);

    HttpResult<List<Category>> getChildrenParallelCategory(Integer categoryId);
}
