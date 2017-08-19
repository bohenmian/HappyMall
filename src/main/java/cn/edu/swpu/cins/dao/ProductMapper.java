package cn.edu.swpu.cins.dao;

import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Product;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> getProductList();

    List<Product> selectNameAndId(@Param("productName") String productName, @Param("productId") Integer productId);

}