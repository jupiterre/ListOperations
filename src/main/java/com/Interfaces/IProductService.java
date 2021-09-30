package com.Interfaces;

import java.util.List;

import com.Models.Product;


public interface IProductService {
    List<Product> getProducts();
    List<Product> sortProducts(String sortType);
    void addProduct(String name, String description);
    void editProduct(int id, String name, String description);
    void deleteProduct(int id);
    List <Product> searchProduct(String s);
    Product getProductById(int id);




}
