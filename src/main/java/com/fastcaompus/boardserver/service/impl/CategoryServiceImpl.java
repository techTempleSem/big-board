package com.fastcaompus.boardserver.service.impl;

import com.fastcaompus.boardserver.dto.CategoryDTO;
import com.fastcaompus.boardserver.mapper.CategoryMapper;
import com.fastcaompus.boardserver.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        if(accountId != null){
            categoryMapper.register(categoryDTO);
        } else {
            log.error("register ERROR! {}", categoryDTO);
            throw new RuntimeException("register ERROR! 등록 메서드 확인 "+categoryDTO);
        }
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        if(categoryDTO != null){
            categoryMapper.updateCategory(categoryDTO);
        } else {
            log.error("register ERROR! {}", categoryDTO);
            throw new RuntimeException("register ERROR! 수정 메서드 확인 "+categoryDTO);
        }
    }

    @Override
    public void delete(int categoryId) {
        if(categoryId != 0){
            categoryMapper.deleteCategory(categoryId);
        } else {
            log.error("register ERROR! {}", categoryId);
            throw new RuntimeException("register ERROR! 삭제 메서드 확인 "+categoryId);
        }
    }
}
