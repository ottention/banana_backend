package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService service;

    @Test
    void testCategoryService() {
        //given
        Long id = 1L;

        //when
        List<CategoryResponse> result = service.getCategories(id);

        //then
        assertThat(result).isNotEmpty();
        result.forEach(c -> System.out.println("c.getCategoryName() = " + c.getCategoryName()));
    }

}