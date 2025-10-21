package com.snack.services;

import com.snack.entities.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class ProductService {

    private String filePath = "C:\\Users\\lieds\\OneDrive\\Documentos\\liedsonpaivadossantos\\Estudos\\Senai\\4semestre\\Teste_De_Sistemas\\BancoImagens";

    private String getFileExtension(Path path) {
        String filename = path.getFileName().toString();
        int lastDotIndex = filename.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }

        return filename.substring(lastDotIndex + 1);
    }

    public String getFilePath() {
        return filePath + (filePath.endsWith("\\") ? "" : "\\");
    }

    /**
     * Salva a imagem do produto.
     * Se a imagem já estiver no destino, apenas atualiza o path no objeto.
     */
    public boolean save(Product product) {
        Path origem = Paths.get(product.getImage());

        // Se o arquivo já estiver na pasta de destino, não precisa copiar
        if (origem.startsWith(getFilePath())) {
            return true;
        }

        Path destino = Paths.get(String.format("%s%d.%s", getFilePath(), product.getId(), getFileExtension(origem)));

        if (Files.exists(origem)) {
            try {
                Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
                product.setImage(destino.toString());
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * Retorna o caminho da imagem do produto pelo ID, ou null se não existir
     */
    public String getImagePathById(int id) {
        File directory = new File(filePath);
        File[] matches = directory.listFiles((dir, name) -> name.startsWith(String.valueOf(id)));

        if (matches == null || matches.length == 0) {
            return null; // não encontrou imagem
        }

        return matches[0].getAbsolutePath();
    }

    /**
     * Atualiza a imagem do produto: remove a antiga (se existir) e salva a nova
     */
    public void update(Product product) {
        // Remover imagem antiga, se existir
        String oldImagePath = getImagePathById(product.getId());
        if (oldImagePath != null) {
            try {
                Files.deleteIfExists(Paths.get(oldImagePath));
            } catch (IOException e) {
                // apenas ignorar se não conseguir deletar
            }
        }

        // Salvar a nova imagem sempre do path atual do produto
        Path origem = Paths.get(product.getImage());
        Path destino = Paths.get(String.format("%s%d.%s", getFilePath(), product.getId(), getFileExtension(origem)));

        try {
            Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
            product.setImage(destino.toString()); // atualiza path no objeto
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar imagem do produto", e);
        }
    }

    /**
     * Remove a imagem do produto pelo ID, se existir
     */
    public void remove(int id) {
        String pathStr = getImagePathById(id);
        if (pathStr == null) return; // não faz nada se não houver imagem

        try {
            Files.deleteIfExists(Paths.get(pathStr));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
