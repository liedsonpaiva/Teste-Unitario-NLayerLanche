package com.snack.facade;

import com.snack.applications.ProductApplication;
import com.snack.entities.Product;
import com.snack.repositories.ProductRepository;
import com.snack.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductFacadeTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private ProductApplication productApplication;
    private ProductFacade productFacade;

    private Product produto1;
    private Product produto2;

    private final String testImagePath = "src/test/resources/test-image.jpg";

    @BeforeEach
    void setup() throws Exception {
        productRepository = new ProductRepository();
        productService = new ProductService();
        productApplication = new ProductApplication(productRepository, productService);
        productFacade = new ProductFacade(productApplication);

        // Criar diretório de imagens
        Files.createDirectories(Paths.get(productService.getFilePath()));

        // Criar produtos
        produto1 = new Product(1, "Produto 1", 10f, testImagePath);
        produto2 = new Product(2, "Produto 2", 20f, testImagePath);

        // Adicionar produtos via Facade (que salva também a imagem)
        productFacade.append(produto1);
        productFacade.append(produto2);
    }

    @Test
    void deveRetornarListaCompletaDeProdutos() {
        List<Product> produtos = productFacade.getAll();
        assertEquals(2, produtos.size(), "Deve retornar todos os produtos cadastrados");
    }

    @Test
    void deveRetornarProdutoPorIdValido() {
        Product resultado = productFacade.getById(1);
        assertNotNull(resultado, "Produto existente deve ser retornado");
        assertEquals("Produto 1", resultado.getDescription());
    }

    @Test
    void deveVerificarExistenciaProduto() {
        assertTrue(productFacade.exists(1), "Produto existente deve retornar true");
        assertFalse(productFacade.exists(999), "Produto inexistente deve retornar false");
    }

    @Test
    void deveAdicionarNovoProduto() throws Exception {
        Product novoProduto = new Product(3, "Produto 3", 30f, testImagePath);
        productFacade.append(novoProduto);

        Product resultado = productFacade.getById(3);
        assertNotNull(resultado, "Novo produto deve ser adicionado corretamente");
        assertEquals("Produto 3", resultado.getDescription());
        assertTrue(Files.exists(Paths.get(resultado.getImage())), "Imagem do novo produto deve existir");
    }

    @Test
    void deveRemoverProdutoExistente() {
        productFacade.remove(produto1.getId());

        assertFalse(productFacade.exists(produto1.getId()), "Produto removido não deve existir mais");
        assertFalse(Files.exists(Paths.get(produto1.getImage())), "Imagem do produto removido deve ser deletada");
    }
}
