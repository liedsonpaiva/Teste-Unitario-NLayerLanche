package com.snack.applications;

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

class ProductApplicationTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private ProductApplication productApplication;

    private Product produto1;
    private Product produto2;

    private final String testImagePath = "src/test/resources/test-image.jpg";

    @BeforeEach
    void inicializarMassasDeTeste() throws Exception {
        productRepository = new ProductRepository();
        productService = new ProductService();
        productApplication = new ProductApplication(productRepository, productService);

        // Garantir que a pasta de imagens exista
        Files.createDirectories(Paths.get(productService.getFilePath()));

        // Criar produtos
        produto1 = new Product(1, "Produto 1", 10f, testImagePath);
        produto2 = new Product(2, "Produto 2", 20f, testImagePath);

        // Copiar imagens para o local do ProductService
        Path destino1 = Paths.get(productService.getFilePath() + produto1.getId() + ".jpg");
        Files.copy(Paths.get(testImagePath), destino1, StandardCopyOption.REPLACE_EXISTING);
        produto1.setImage(destino1.toString());

        Path destino2 = Paths.get(productService.getFilePath() + produto2.getId() + ".jpg");
        Files.copy(Paths.get(testImagePath), destino2, StandardCopyOption.REPLACE_EXISTING);
        produto2.setImage(destino2.toString());

        // Adicionar produtos no sistema
        productApplication.append(produto1);
        productApplication.append(produto2);
    }

    @Test
    void testarListarTodosOsProdutos() {
        List<Product> produtos = productApplication.getAll();
        assertEquals(2, produtos.size(), "Deve retornar todos os produtos cadastrados");
    }

    @Test
    void validarObterProdutoPorIdValido() {
        Product resultado = productApplication.getById(1);
        assertNotNull(resultado, "Produto existente deve ser retornado");
        assertEquals("Produto 1", resultado.getDescription());
    }

    @Test
    void tratarObterProdutoPorIdInvalidoRetornaNulo() {
        Product resultado = productApplication.getById(999);
        assertNull(resultado, "Produto inexistente deve retornar nulo");
    }

    @Test
    void testarExistenciaProdutoPorIdValido() {
        assertTrue(productApplication.exists(1), "Produto existente deve retornar true");
    }

    @Test
    void validarNaoExistenciaProdutoPorIdInvalido() {
        assertFalse(productApplication.exists(999), "Produto inexistente deve retornar false");
    }

    @Test
    void testarAdicionarProdutoESalvarImagem() throws Exception {
        Product novoProduto = new Product(3, "Produto 3", 30f, testImagePath);

        // Copiar imagem para pasta do ProductService
        Path destino = Paths.get(productService.getFilePath() + novoProduto.getId() + ".jpg");
        Files.copy(Paths.get(testImagePath), destino, StandardCopyOption.REPLACE_EXISTING);
        novoProduto.setImage(destino.toString());

        productApplication.append(novoProduto);

        assertTrue(Files.exists(Paths.get(novoProduto.getImage())), "Imagem do produto deve ser salva corretamente");
    }

    @Test
    void testarRemoverProdutoExistenteEDeletarImagem() {
        String imagePath = produto1.getImage();
        productApplication.remove(produto1.getId());

        assertFalse(productApplication.exists(produto1.getId()), "Produto removido não deve existir mais");
        assertFalse(Files.exists(Paths.get(imagePath)), "Imagem do produto removido deve ser deletada");
    }

    @Test
    void tratarRemoverProdutoInexistenteNaoAlteraSistema() {
        assertDoesNotThrow(() -> productApplication.remove(999), "Remover produto inexistente não deve lançar erro");
    }

    @Test
    void testarAtualizarProdutoExistenteESubstituirImagem() throws Exception {
        Product atualizado = new Product(1, "Produto Atualizado", 50f, testImagePath);

//        // Copiar imagem nova para pasta do ProductService
//        Path destino = Paths.get(productService.getFilePath() + atualizado.getId() + ".jpg");
//        Files.copy(Paths.get(testImagePath), destino, StandardCopyOption.REPLACE_EXISTING);
//        atualizado.setImage(destino.toString());

        productApplication.update(1, atualizado);

        Product resultado = productApplication.getById(1);
        assertEquals("Produto Atualizado", resultado.getDescription());
        assertEquals(50f, resultado.getPrice());
        assertTrue(Files.exists(Paths.get(resultado.getImage())), "Imagem do produto atualizado deve existir");
    }
}
