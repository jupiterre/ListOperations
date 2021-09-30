package com.Controllers;

import com.Models.Product;
import com.Services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class Controllers {

    private final ProductService productService = new ProductService();

    @GetMapping("/")
    public String index(Model model) {
        List<Product> productList = productService.getProducts();
        System.out.println("prod list" + productList);
        model.addAttribute("productList", productList);
        return "index"; // resources/templates/index.html
    }

    @PostMapping("/")
    public String addProductForm(@RequestParam String name, @RequestParam String description, Model model) {
        List<Product> productList = new ArrayList<>();
        try {
            productService.addProduct(name, description);

            productList = productService.getProducts();

            model.addAttribute("successMessage", "Your product has been added");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("productList", productList);
        return "index"; // resources/templates/index.html
    }

    @GetMapping("/search")
    public String search(@RequestParam String term, Model model) {
        System.out.println("searching");
        List<Product> productList = productService.getProducts();
        System.out.println("got products");
        for (int i = 0; i < productList.size(); i++) {
            System.out.println(productList.get(i));
        }

        try {
            System.out.println("starting search");
            productList = productService.searchProduct(term);
            System.out.println("ending sort");
            for (int i = 0; i < productList.size(); i++) {
                System.out.println(productList.get(i));
            }
        } catch (Exception e) {
            System.out.println("exception up here");
            System.out.println(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("productList", productList);
        return "index"; // resources/templates/index.html
    }

    @GetMapping("/products/edit/{id}")
    public String editPage(@PathVariable int id, Model model) {

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit";
    }

    @PostMapping("/edit-product")
    public String editProductForm(@RequestParam String name,
                               @RequestParam String description,
                               @RequestParam(required = false) int id,
                               Model model) {

        try {

            productService.editProduct(id, name, description);

            model.addAttribute("successMessage", "Changes to the product were saved.");
            model.addAttribute("productList", productService.getProducts());
            return "index";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("product", productService.getProductById(id));
        }

        return "edit";
    }

    @GetMapping("/products/delete/{id}")
    public String deletePage(@PathVariable int id, Model model) {
        List<Product> productList = new ArrayList<>();
        try {

            productService.deleteProduct(id);


            productList = productService.getProducts();

            model.addAttribute("successMessage", "Your product has been deleted");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("productList", productList);
        return "index"; // resources/templates/index.html
    }

    @GetMapping("/sortname")
    public String sortname(Model model) {
        List<Product> productList = productService.getProducts();

        try {
            productList = productService.sortProducts("name");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("productList", productList);
        return "index"; // resources/templates/index.html
    }

    @GetMapping("/sortid")
    public String sortid(Model model) {
        List<Product> productList = productService.getProducts();

        try {
            productList = productService.sortProducts("id");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("productList", productList);
        return "index"; // resources/templates/index.html
    }
}
