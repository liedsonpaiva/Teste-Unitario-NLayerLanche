package com.snack.repositories;

import com.snack.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class ProductRepositoryTest {

    private ProductRepository repository;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void configurarMassaDeTeste() {
        repository = new ProductRepository();
        product1 = new Product(1, "Hambúrguer", 20.0f, "hamburguer.jpg");
        product2 = new Product(2, "Refrigerante", 8.0f, "refri.jpg");
        repository.getAll().add(product1);
        repository.getAll().add(product2);
    }

    @Test
    public void deveAdicionarProdutoNoRepositorio() {
        Product novo = new Product(3, "Batata Frita", 12.0f, "batata.jpg");
        repository.getAll().add(novo);

        Assertions.assertTrue(repository.getAll().contains(novo));
        Assertions.assertEquals(3, repository.getAll().size());
    }

    @Test
    public void deveRecuperarProdutoPorId() {
        Product encontrado = repository.getById(1);
        Assertions.assertEquals("Hambúrguer", encontrado.getDescription());
    }

    @Test
    public void deveConfirmarExistenciaDeProdutoPorId() {
        boolean existe = repository.exists(2);
        Assertions.assertTrue(existe);
    }

    @Test
    public void deveRemoverProdutoPorId() {
        repository.remove(1);
        Assertions.assertEquals(1, repository.getAll().size());
        Assertions.assertFalse(repository.exists(1));
    }

    @Test
    public void deveAtualizarProdutoCorretamente() {
        Product novo = new Product(1, "X-Burguer", 25.0f, "xburguer.jpg");
        repository.update(1, novo);

        Product atualizado = repository.getById(1);
        Assertions.assertEquals("X-Burguer", atualizado.getDescription());
        Assertions.assertEquals(25.0f, atualizado.getPrice());
    }

    @Test
    public void deveRecuperarTodosOsProdutos() {
        List<Product> produtos = repository.getAll();
        Assertions.assertEquals(2, produtos.size());
    }

    @Test
    public void deveTratarRemocaoDeProdutoInexistente() {
        repository.remove(999);
        Assertions.assertEquals(2, repository.getAll().size());
    }

    @Test
    public void deveTratarAtualizacaoDeProdutoInexistente() {
        Product inexistente = new Product(999, "Pizza", 30.0f, "pizza.jpg");

        Assertions.assertThrows(Exception.class, () -> {
            repository.update(999, inexistente);
        });
    }

    @Test
    public void deveAceitarIdsDuplicadosNaLista() {
        Product duplicado = new Product(1, "Hambúrguer 2", 20.0f, "hamb2.jpg");
        repository.getAll().add(duplicado);

        long count = repository.getAll().stream().filter(p -> p.getId() == 1).count();
        Assertions.assertTrue(count > 1, "IDs duplicados foram aceitos");
    }

    @Test
    public void deveRetornarListaVaziaQuandoInicializado() {
        ProductRepository novoRepo = new ProductRepository();
        Assertions.assertTrue(novoRepo.getAll().isEmpty());
    }
}
