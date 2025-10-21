package com.snack.services;

import com.snack.entities.Product;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private ProductService service;
    private Path tempDir;

    @BeforeEach
    public void configurarAmbienteDeTeste() throws IOException {
        service = new ProductService();

        // Cria um diretório temporário isolado para os testes
        tempDir = Files.createTempDirectory("test_images_");

        // Usa reflexão para alterar o caminho privado "filePath"
        try {
            Field field = ProductService.class.getDeclaredField("filePath");
            field.setAccessible(true);
            field.set(service, tempDir.toString() + File.separator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deveSalvarProdutoComImagemValida() throws IOException {
        Path imagem = Files.createTempFile("produto1_", ".jpg");

        Product produto = new Product(1, "Sanduíche", 15.0f, imagem.toString());
        boolean resultado = service.save(produto);

        assertTrue(resultado, "Deveria salvar o produto com imagem válida");
        assertTrue(Files.exists(Paths.get(produto.getImage())), "Imagem copiada deveria existir");
    }

    @Test
    public void deveFalharAoSalvarProdutoComImagemInexistente() {
        Product produto = new Product(2, "Suco", 10.0f, "caminho/inexistente.jpg");
        boolean resultado = service.save(produto);

        assertFalse(resultado, "Não deveria salvar imagem inexistente");
    }

    @Test
    public void deveAtualizarProdutoExistente() throws IOException {
        Path imagemInicial = Files.createTempFile("produto2_", ".jpg");
        Product produto = new Product(2, "Coxinha", 8.0f, imagemInicial.toString());
        service.save(produto);

        Path novaImagem = Files.createTempFile("produto2_nova_", ".jpg");
        produto.setImage(novaImagem.toString());

        service.update(produto);

        Path destino = Paths.get(tempDir.toString(), "2.jpg");
        assertTrue(Files.exists(destino), "Deveria existir a nova imagem após atualização");
    }

    @Test
    public void deveRemoverProdutoExistente() throws IOException {
        Path imagem = Files.createTempFile("produto3_", ".jpg");
        Product produto = new Product(3, "Pastel", 7.0f, imagem.toString());
        service.save(produto);

        service.remove(produto.getId());

        Path destino = Paths.get(tempDir.toString(), "3.jpg");
        assertFalse(Files.exists(destino), "Imagem deveria ser removida do diretório");
    }

    @Test
    public void deveObterCaminhoDaImagemPorId() throws IOException {
        Path imagem = Files.createTempFile("produto4_", ".jpg");
        Product produto = new Product(4, "Empada", 6.0f, imagem.toString());
        service.save(produto);

        String caminhoEncontrado = service.getImagePathById(produto.getId());

        assertNotNull(caminhoEncontrado, "Caminho não deveria ser nulo");
        assertTrue(caminhoEncontrado.endsWith("4.jpg"), "Deveria retornar a imagem do produto 4");
    }

    @AfterEach
    public void limparDiretorioTemporario() throws IOException {
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted((a, b) -> b.compareTo(a))
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}
