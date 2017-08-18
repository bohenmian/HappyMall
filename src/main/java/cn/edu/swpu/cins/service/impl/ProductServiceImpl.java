package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.ProductMapper;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Product;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.service.ProduceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProduceService {

    private ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public HttpResult saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String [] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return HttpResult.createBySuccess("update product success");
                }
                return HttpResult.createByErrorMessage("update product fail");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return HttpResult.createBySuccess("add product success");
                }
                return HttpResult.createByErrorMessage("add product fail");
            }
        }
        return HttpResult.createByErrorMessage("paramter is wrong");
    }

    public HttpResult<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.ILLEGAL_ARGUMENT.getCode(), HttpResultEnum.ILLEGAL_ARGUMENT.getDescrption());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return HttpResult.createBySuccess("update product status success");
        }
        return HttpResult.createByErrorMessage("update product status fail");
    }
}
