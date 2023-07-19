package com.ottention.banana.service;

import com.ottention.banana.dto.response.businesscard.CategoryResponse;
import com.ottention.banana.entity.User;
import com.ottention.banana.entity.wallet.Category;
import com.ottention.banana.repository.UserRepository;
import com.ottention.banana.repository.wallet.CategoryRepository;
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
    @Autowired
    private CategoryRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testCategoryService() {
        //given
        User user = User.builder()
                .email("a")
                .nickName("a")
                .build();
        userRepository.save(user);
        Category category = new Category("category-name", user);
        repository.save(category);

        //when
        List<CategoryResponse> result = service.getCategories(user.getId());

        //then
        assertThat(result).isNotEmpty();
    }

}