package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.config.DateTimeDeserializer;
import cn.edu.swpu.cins.config.PropertiesConfig;
import cn.edu.swpu.cins.dao.CategoryMapper;
import cn.edu.swpu.cins.dao.ProductMapper;
import cn.edu.swpu.cins.dto.request.ProductDetail;
import cn.edu.swpu.cins.dto.request.ProductVo;
import cn.edu.swpu.cins.dto.response.Const;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Category;
import cn.edu.swpu.cins.entity.Product;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.enums.ProductStatusEnum;
import cn.edu.swpu.cins.exception.HappyMallException;
import cn.edu.swpu.cins.exception.ProductNoExitedException;
import cn.edu.swpu.cins.service.CategoryService;
import cn.edu.swpu.cins.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductMapper productMapper;
    private CategoryMapper categoryMapper;
    private CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper,
                              CategoryService categoryService) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.categoryService = categoryService;
    }

    @Transactional(rollbackFor = DataAccessException.class)
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
        return HttpResult.createByErrorMessage("parameter is illegal");
    }

    @Transactional
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

    public HttpResult<ProductDetail > manageProductDetail(Integer productId) {
        if (productId == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.ILLEGAL_ARGUMENT.getCode(), HttpResultEnum.ILLEGAL_ARGUMENT.getDescrption());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            throw new ProductNoExitedException("product not exit");
        }
        ProductDetail productDetail = assembleProductDetail(product);
        return HttpResult.createBySuccess(productDetail);

    }

    private ProductDetail assembleProductDetail(Product product) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(product.getId());
        productDetail.setSubtitle(product.getSubtitle());
        productDetail.setPrice(product.getPrice());
        productDetail.setMainImage(product.getMainImage());
        productDetail.setSubImages(product.getSubImages());
        productDetail.setCategoryId(product.getCategoryId());
        productDetail.setDetail(product.getDetail());
        productDetail.setName(product.getName());
        productDetail.setStatus(product.getStatus());
        productDetail.setStock(product.getStock());
        productDetail.setImageHost(PropertiesConfig.getProperties("ftp.server.http.prefix", "http://img.happymall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getId());
        if (category == null) {
            productDetail.setParentCategoryId(0);
        } else {
            productDetail.setParentCategoryId(category.getParentId());
        }
        productDetail.setCreateTime(DateTimeDeserializer.dateToStr(product.getCreateTime()));
        productDetail.setUpdateTime(DateTimeDeserializer.dateToStr(product.getUpdateTime()));
        return productDetail;
    }

    public HttpResult<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.getProductList();
        List<ProductVo> productVoList = Lists.newArrayList();
        for (Product productItem : list) {
            ProductVo productVo = assembleProductList(productItem);
            productVoList.add(productVo);
        }
        PageInfo pageResult = new PageInfo(list);
        pageResult.setList(productVoList);
        return HttpResult.createBySuccess(pageResult);
    }

    private ProductVo assembleProductList(Product product) {
        ProductVo productVo = new ProductVo();
        productVo.setId(product.getId());
        productVo.setName(product.getName());
        productVo.setCategoryId(product.getCategoryId());
        productVo.setImageHost(PropertiesConfig.getProperties("ftp.server.http.prefix", "http://img.happymall.com/"));
        productVo.setMainImage(product.getMainImage());
        productVo.setPrice(product.getPrice());
        productVo.setSubtitle(product.getSubtitle());
        productVo.setStatus(product.getStatus());
        return productVo;
    }

    public HttpResult<PageInfo> searchProduct(String productName, Integer prodcutId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append("%").toString();
        }

        List<Product> list = productMapper.selectByNameAndproductId(productName, prodcutId);
        List<ProductVo> productVoList = Lists.newArrayList();
        for (Product productItem : list) {
            ProductVo productVo = assembleProductList(productItem);
            productVoList.add(productVo);
        }
        PageInfo pageResult = new PageInfo(list);
        pageResult.setList(productVoList);
        return HttpResult.createBySuccess(pageResult);
    }

    public HttpResult<ProductDetail> getProductDetail(Integer productId) {
        if (productId == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return HttpResult.createByErrorMessage("search product not exited");
        }
        if (product.getStatus() != ProductStatusEnum.ON_SALE.getCode()) {
            return HttpResult.createByErrorMessage("product had undercarriage");
        }
        ProductDetail productDetail = assembleProductDetail(product);
        return HttpResult.createBySuccess(productDetail);
    }

    public HttpResult<PageInfo> getProductByKeyword(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.ILLEGAL_ARGUMENT.getCode(), HttpResultEnum.ILLEGAL_ARGUMENT.getDescrption());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductVo> productVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productVoList);
                return HttpResult.createBySuccess(pageInfo);
            }
        categoryIdList = categoryService.getCategory(categoryId).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryId(StringUtils.isNotBlank(keyword) ? null : keyword,
                 categoryIdList.size() == 0 ? null : categoryIdList);
        List<ProductVo> productVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductVo productVo = assembleProductList(product);
            productVoList.add(productVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productVoList);
        return HttpResult.createBySuccess(pageInfo);
    }

}
