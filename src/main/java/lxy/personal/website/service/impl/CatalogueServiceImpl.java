package lxy.personal.website.service.impl;

import lxy.personal.website.dao.CatalogueMapper;
import lxy.personal.website.entity.Catalogue;
import lxy.personal.website.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lxy
 * @Date 2021/10/31 17:33
 * @Description
 */
@Service
public class CatalogueServiceImpl implements CatalogueService {

    @Autowired
    private CatalogueMapper catalogueMapper;

    @Override
    public List<Catalogue> getAllCata() {
        return catalogueMapper.getAllCata();
    }
}
