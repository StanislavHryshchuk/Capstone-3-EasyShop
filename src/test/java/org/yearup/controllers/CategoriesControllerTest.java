package org.yearup.controllers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CategoriesControllerTest {
    private CategoryDao categoryDao;
    private ProductDao productDao;
    private CategoriesController restController;


    @BeforeEach
    public void setup() {
        categoryDao = mock(CategoryDao.class);
        productDao = mock(ProductDao.class);
        restController = new CategoriesController(categoryDao, productDao);
    }

    @Test
    public void testGetAllCategories() {
        Category category = new Category() {{
            setCategoryId(1);
            setName("Electronics");
            setDescription("Everything related to PC");
        }};
        Category category1 = new Category(){{
            setCategoryId(2);
            setName("Outdoor");
            setDescription("Everything you need for outdoor activities");
        }};
        when(categoryDao.getAllCategories()).thenReturn(Arrays.asList(category, category1));

        List<Category> result = restController.getAll();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Everything you need for outdoor activities", result.get(1).getDescription());
    }

    @Test
    public void testGetById_found() {
        Category cat = new Category() {{
            setCategoryId(1);
            setName("Books");
            setDescription("Books category");
        }};
        when(categoryDao.getById(1)).thenReturn(cat);

        Category result = restController.getById(1);

        assertNotNull(result);
        assertEquals("Books", result.getName());
        assertEquals("Books category", result.getDescription());
    }

    @Test
    public void testGetById_notFound() {
        when(categoryDao.getById(1)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            restController.getById(1);
        });
    }

    @Test
    public void testGetProductsByCategoryId() {
        Product product = new Product() {{
            setProductId(1);
            setName("Laptop");
        }};
        when(productDao.listByCategoryId(1)).thenReturn(List.of(product));

        List<Product> result = restController.getProductsById(1);
        assertEquals(1, result.size());
    }

    @Test
    public void testAddCategory() {
        Category cat = new Category() {{
            setName("New Category");
            setDescription("Desc");
        }};
        when(categoryDao.create(cat)).thenReturn(cat);

        Category result = restController.addCategory(cat);

        assertEquals("New Category", result.getName());
    }

    @Test
    public void testUpdateCategory() {
        Category cat = new Category() {{
            setName("Updated");
            setDescription("Updated Desc");
        }};
        when(categoryDao.update(1, cat)).thenReturn(cat);

        Category result = restController.updateCategory(1, cat);

        assertEquals("Updated", result.getName());
    }

    @Test
    public void testDeleteCategory() {
        doNothing().when(categoryDao).delete(1);

        assertDoesNotThrow(() -> restController.deleteCategory(1));
    }
}
