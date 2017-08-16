package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.response.HttpResult;

public interface CategoryService {

    HttpResult addCategory(String categoryName, Integer parentId);

    HttpResult updateCategoryName(String categoryName, Integer parentId);
}
