package by.bubalehich.pm.service;

import by.bubalehich.pm.dto.CreateProductDto;

import java.util.concurrent.ExecutionException;

public interface ProductService {
    String createProduct(CreateProductDto dto) throws ExecutionException, InterruptedException;
}
