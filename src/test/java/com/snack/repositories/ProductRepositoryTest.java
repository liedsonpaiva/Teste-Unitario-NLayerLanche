package com.snack.repositories;

import com.snack.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryTest {

    private final List<Product> products = new ArrayList<>();

    public List<Product> getAll() {
        return products;
    }

    public Product getById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean exists(int id) {
        return products.stream()
                .anyMatch(p -> p.getId() == id);
    }

    public void append(Product product) {
        products.add(product);
    }

    public void remove(int id) {
        products.removeIf(product -> product.getId() == id);
    }

    public void update(int id, Product product) throws Exception {
        Product productInDb = products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (productInDb != null) {
            productInDb.setDescription(product.getDescription());
            productInDb.setPrice(product.getPrice());
            productInDb.setImage(product.getImage());
            return; // impede que a exceção seja lançada quando o produto é encontrado
        }

        throw new Exception("Produto com ID " + id + " não encontrado para atualização.");
    }
}
