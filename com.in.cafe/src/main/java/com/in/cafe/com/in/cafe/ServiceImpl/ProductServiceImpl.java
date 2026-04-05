package com.in.cafe.com.in.cafe.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.in.cafe.com.in.cafe.JWT.JwtFilter;
import com.in.cafe.com.in.cafe.POJO.Category;
import com.in.cafe.com.in.cafe.POJO.Product;
import com.in.cafe.com.in.cafe.Service.ProductService;
import com.in.cafe.com.in.cafe.constents.CafeConstants;
import com.in.cafe.com.in.cafe.dao.CategoryDao;
import com.in.cafe.com.in.cafe.dao.ProductDao;
import com.in.cafe.com.in.cafe.utils.CafeUtils;
import com.in.cafe.com.in.cafe.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            if (!validateProductMap(requestMap, false)) {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            Product product = getProductFromMap(requestMap, false);
            productDao.save(product);

            return CafeUtils.getResponseEntity("Product Added Successfully", HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            if (!validateProductMap(requestMap, true)) {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            Integer id = (Integer) requestMap.get("id");
            Optional<Product> optional = productDao.findById(id);
            if (optional.isEmpty()) {
                return CafeUtils.getResponseEntity("Product id does not exist", HttpStatus.BAD_REQUEST);
            }

            Product product = getProductFromMap(requestMap, true);
            product.setStatus(optional.get().getStatus()); // preserve status
            productDao.save(product);

            return CafeUtils.getResponseEntity("Product Updated Successfully", HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            Optional<Product> optional = productDao.findById(id);
            if (optional.isEmpty()) {
                return CafeUtils.getResponseEntity("Product id does not exist", HttpStatus.BAD_REQUEST);
            }

            productDao.deleteById(id);
            return CafeUtils.getResponseEntity("Product Deleted Successfully", HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, Object> requestMap) {
        try {
            if (!jwtFilter.isAdmin()) {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            Integer id = (Integer) requestMap.get("id");
            Optional<Product> optional = productDao.findById(id);
            if (optional.isEmpty()) {
                return CafeUtils.getResponseEntity("Product id does not exist", HttpStatus.BAD_REQUEST);
            }

            String status = (String) requestMap.get("status");
            productDao.updateProductStatus(status, id);

            return CafeUtils.getResponseEntity("Product Status Updated Successfully", HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ----------------- Helper Methods -----------------
    private boolean validateProductMap(Map<String, Object> requestMap, boolean validateId) {
        if (!requestMap.containsKey("name") || !requestMap.containsKey("categoryId") || !requestMap.containsKey("price")) {
            return false;
        }
        if (validateId && !requestMap.containsKey("id")) {
            return false;
        }
        return true;
    }

    private Product getProductFromMap(Map<String, Object> requestMap, boolean isUpdate) {
        Product product = new Product();

        Integer categoryId = (Integer) requestMap.get("categoryId");
        Optional<Category> optionalCategory = categoryDao.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new RuntimeException("Category does not exist");
        }

        Category category = optionalCategory.get();

        if (isUpdate) {
            product.setId((Integer) requestMap.get("id"));
        } else {
            product.setStatus("true"); // default status
        }

        product.setCategory(category);
        product.setName((String) requestMap.get("name"));
        product.setPrice((Integer) requestMap.get("price"));
        if (requestMap.containsKey("description")) {
            product.setDescription((String) requestMap.get("description"));
        }

        return product;
    }

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		// TODO Auto-generated method stub
		return null;
	}
}