package com.Services;

import com.mongodb.ConnectionString;
import com.mongodb.DBCursor;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import org.bson.conversions.Bson;
import com.Interfaces.IProductService;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.stereotype.Service;
import com.Models.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ProductService implements IProductService {


    ConnectionString connectionString = new ConnectionString("mongodb+srv://adminErin:admin1@cluster0.rlqim.mongodb.net/ListOps?retryWrites=true&w=majority");
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
    com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings);

    //com.mongodb.client.MongoClient mongoClient = MongoClients.create("mongodb+srv://adminErin:admin1@cluster0.rlqim.mongodb.net/ListOps?retryWrites=true&w=majority");
    MongoDatabase database = mongoClient.getDatabase("ListOps");
    MongoCollection<org.bson.Document> productsCollection = database.getCollection("Products");
    FindIterable<org.bson.Document> productsColl = productsCollection.find();

    @Override
    public List<Product> getProducts() {
        List<Product> allProducts = new ArrayList<>();
        MongoCursor<org.bson.Document> cursor = productsColl.iterator();
        System.out.println("cursor thing" + cursor.toString());

        try {
            while(cursor.hasNext()) {
                //System.out.println("NEXT");
                org.bson.Document doc = cursor.next();
                //System.out.println("doc" + doc.toString());
                Integer id = (Integer) doc.get("id");
                //System.out.println("id" + id.toString());
                String name = (String) doc.get("name");
                //System.out.println("name" + name.toString());
                String description = (String) doc.get("description");
                //System.out.println("description" + description.toString());
                String imgName = (String) doc.get("imageName");
                //System.out.println("imgName" + imgName.toString());
                //Product item = (Product) JSON.parse(doc.toJson());
                Product item = new Product(id, name, description, imgName);
                //System.out.println("new product made");

                //System.out.println("item" + item.toString());
                allProducts.add(item);
                //System.out.println("ITEM ADDED");
            }
        } catch (Exception ex) {
            //blah blah
            System.out.println("exception");
        } finally {
            //System.out.println("closing");
            cursor.close();
        }

        //System.out.println("returning all products");
        return allProducts;
    }

    @Override
    public List<Product> sortProducts(String sortType) {
        List<Product> unsorted = getProducts();

        if (sortType.equalsIgnoreCase("id")) {
            Collections.sort(unsorted, new IdComparator());
        } else if (sortType.equalsIgnoreCase("name")){
            Collections.sort(unsorted, new NameComparator());
        }

        return unsorted;
    }

    public class IdComparator implements Comparator<Product> {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.getId() - o2.getId();
        }
    }

    public class NameComparator implements Comparator<Product> {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    @Override
    public void addProduct(String name, String description) {
        List<Product> allProducts = sortProducts("id");
        Product lastProduct = allProducts.get(allProducts.size() - 1);
        int id = lastProduct.getId();
        id += 1;

        org.bson.Document doc = new org.bson.Document();
        doc.put("id", id);
        doc.put("name", name);
        doc.put("description", description);
        productsCollection.insertOne(doc);
    }

    @Override
    public void editProduct(int id, String name, String description) {
        Bson filter = Filters.eq("id", id);
        Bson update = Filters.and(
                Filters.eq("name", name),
                Filters.eq("description", description)
        );
        productsCollection.findOneAndUpdate(filter, update);

    }

    @Override
    public void deleteProduct(int id) {
        Bson filter = Filters.eq("id", id);
        productsCollection.findOneAndDelete(filter);
    }

    @Override
    public List <Product> searchProduct(String s) {
        String patternStr = "." + s + ".";
        String patternString = s + ".";
        String patternStr2 = "." + s;


        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Pattern pattern3 = Pattern.compile(patternStr2, Pattern.CASE_INSENSITIVE);

        int i;
        try {
            i  = Integer.parseInt(s);
        } catch(NumberFormatException nfe) {
            // Log exception.
            i = 0;
        }

        Bson filter = Filters.or (
                Filters.regex("name", pattern),
                Filters.regex("name", pattern2),
                Filters.regex("name", pattern3),
                Filters.eq("name", s),
                Filters.eq("id", i)
                //Filters.text(s)
        );


        FindIterable<org.bson.Document> productsCollec = productsCollection.find(filter);

        List<Product> products = new ArrayList<>();


        for(org.bson.Document prod : productsCollec) {

            Integer id = (Integer) prod.get("id");

            String name = (String) prod.get("name");

            String description = (String) prod.get("description");

            String imgName = (String) prod.get("imageName");

            Product item = new Product(id, name, description, imgName);

            products.add(item);

        }



        return products;
    }

    @Override
    public Product getProductById(int id) {
        Bson filter = Filters.eq("id", id);

        FindIterable<org.bson.Document> productsColl = productsCollection.find(filter);

        List<Product> products = new ArrayList<>();
        MongoCursor<org.bson.Document> cursor = productsColl.iterator();

        try {
            while(cursor.hasNext()) {
                //System.out.println("NEXT");
                org.bson.Document doc = cursor.next();
                //System.out.println("doc" + doc.toString());
                Integer Id = (Integer) doc.get("id");
                //System.out.println("id" + id.toString());
                String name = (String) doc.get("name");
                //System.out.println("name" + name.toString());
                String description = (String) doc.get("description");
                //System.out.println("description" + description.toString());
                String imgName = (String) doc.get("imageName");
                //System.out.println("imgName" + imgName.toString());
                //Product item = (Product) JSON.parse(doc.toJson());
                Product item = new Product(Id, name, description, imgName);
                //System.out.println("new product made");

                //System.out.println("item" + item.toString());
                products.add(item);
                //System.out.println("ITEM ADDED");
            }
        } catch (Exception ex) {
            //blah blah
            System.out.println("exception");
        } finally {
            //System.out.println("closing");
            cursor.close();
        }

        //should only be one, but switching between documents and models is a pain
        return products.get(0);

    }


}
